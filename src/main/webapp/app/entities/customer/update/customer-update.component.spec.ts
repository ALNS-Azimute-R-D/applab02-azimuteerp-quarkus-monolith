import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICustomerType } from 'app/entities/customer-type/customer-type.model';
import { CustomerTypeService } from 'app/entities/customer-type/service/customer-type.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { ICustomer } from '../customer.model';
import { CustomerService } from '../service/customer.service';
import { CustomerFormService } from './customer-form.service';

import { CustomerUpdateComponent } from './customer-update.component';

describe('Customer Management Update Component', () => {
  let comp: CustomerUpdateComponent;
  let fixture: ComponentFixture<CustomerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerFormService: CustomerFormService;
  let customerService: CustomerService;
  let customerTypeService: CustomerTypeService;
  let districtService: DistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CustomerUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CustomerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerFormService = TestBed.inject(CustomerFormService);
    customerService = TestBed.inject(CustomerService);
    customerTypeService = TestBed.inject(CustomerTypeService);
    districtService = TestBed.inject(DistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CustomerType query and add missing value', () => {
      const customer: ICustomer = { id: 456 };
      const customerType: ICustomerType = { id: 28641 };
      customer.customerType = customerType;

      const customerTypeCollection: ICustomerType[] = [{ id: 9228 }];
      jest.spyOn(customerTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: customerTypeCollection })));
      const additionalCustomerTypes = [customerType];
      const expectedCollection: ICustomerType[] = [...additionalCustomerTypes, ...customerTypeCollection];
      jest.spyOn(customerTypeService, 'addCustomerTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      expect(customerTypeService.query).toHaveBeenCalled();
      expect(customerTypeService.addCustomerTypeToCollectionIfMissing).toHaveBeenCalledWith(
        customerTypeCollection,
        ...additionalCustomerTypes.map(expect.objectContaining),
      );
      expect(comp.customerTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call District query and add missing value', () => {
      const customer: ICustomer = { id: 456 };
      const district: IDistrict = { id: 1447 };
      customer.district = district;

      const districtCollection: IDistrict[] = [{ id: 14680 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        districtCollection,
        ...additionalDistricts.map(expect.objectContaining),
      );
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customer: ICustomer = { id: 456 };
      const customerType: ICustomerType = { id: 18143 };
      customer.customerType = customerType;
      const district: IDistrict = { id: 8674 };
      customer.district = district;

      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      expect(comp.customerTypesSharedCollection).toContain(customerType);
      expect(comp.districtsSharedCollection).toContain(district);
      expect(comp.customer).toEqual(customer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomer>>();
      const customer = { id: 123 };
      jest.spyOn(customerFormService, 'getCustomer').mockReturnValue(customer);
      jest.spyOn(customerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customer }));
      saveSubject.complete();

      // THEN
      expect(customerFormService.getCustomer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerService.update).toHaveBeenCalledWith(expect.objectContaining(customer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomer>>();
      const customer = { id: 123 };
      jest.spyOn(customerFormService, 'getCustomer').mockReturnValue({ id: null });
      jest.spyOn(customerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customer }));
      saveSubject.complete();

      // THEN
      expect(customerFormService.getCustomer).toHaveBeenCalled();
      expect(customerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomer>>();
      const customer = { id: 123 };
      jest.spyOn(customerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomerType', () => {
      it('Should forward to customerTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerTypeService, 'compareCustomerType');
        comp.compareCustomerType(entity, entity2);
        expect(customerTypeService.compareCustomerType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDistrict', () => {
      it('Should forward to districtService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(districtService, 'compareDistrict');
        comp.compareDistrict(entity, entity2);
        expect(districtService.compareDistrict).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
