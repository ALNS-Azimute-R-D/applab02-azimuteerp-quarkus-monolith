import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { BusinessUnitService } from '../service/business-unit.service';
import { IBusinessUnit } from '../business-unit.model';
import { BusinessUnitFormService } from './business-unit-form.service';

import { BusinessUnitUpdateComponent } from './business-unit-update.component';

describe('BusinessUnit Management Update Component', () => {
  let comp: BusinessUnitUpdateComponent;
  let fixture: ComponentFixture<BusinessUnitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessUnitFormService: BusinessUnitFormService;
  let businessUnitService: BusinessUnitService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BusinessUnitUpdateComponent],
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
      .overrideTemplate(BusinessUnitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessUnitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessUnitFormService = TestBed.inject(BusinessUnitFormService);
    businessUnitService = TestBed.inject(BusinessUnitService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Organization query and add missing value', () => {
      const businessUnit: IBusinessUnit = { id: 456 };
      const organization: IOrganization = { id: 6787 };
      businessUnit.organization = organization;

      const organizationCollection: IOrganization[] = [{ id: 12110 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [organization];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessUnit });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining),
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BusinessUnit query and add missing value', () => {
      const businessUnit: IBusinessUnit = { id: 456 };
      const businessUnitParent: IBusinessUnit = { id: 19954 };
      businessUnit.businessUnitParent = businessUnitParent;

      const businessUnitCollection: IBusinessUnit[] = [{ id: 17385 }];
      jest.spyOn(businessUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: businessUnitCollection })));
      const additionalBusinessUnits = [businessUnitParent];
      const expectedCollection: IBusinessUnit[] = [...additionalBusinessUnits, ...businessUnitCollection];
      jest.spyOn(businessUnitService, 'addBusinessUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessUnit });
      comp.ngOnInit();

      expect(businessUnitService.query).toHaveBeenCalled();
      expect(businessUnitService.addBusinessUnitToCollectionIfMissing).toHaveBeenCalledWith(
        businessUnitCollection,
        ...additionalBusinessUnits.map(expect.objectContaining),
      );
      expect(comp.businessUnitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const businessUnit: IBusinessUnit = { id: 456 };
      const organization: IOrganization = { id: 18876 };
      businessUnit.organization = organization;
      const businessUnitParent: IBusinessUnit = { id: 18802 };
      businessUnit.businessUnitParent = businessUnitParent;

      activatedRoute.data = of({ businessUnit });
      comp.ngOnInit();

      expect(comp.organizationsSharedCollection).toContain(organization);
      expect(comp.businessUnitsSharedCollection).toContain(businessUnitParent);
      expect(comp.businessUnit).toEqual(businessUnit);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessUnit>>();
      const businessUnit = { id: 123 };
      jest.spyOn(businessUnitFormService, 'getBusinessUnit').mockReturnValue(businessUnit);
      jest.spyOn(businessUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessUnit }));
      saveSubject.complete();

      // THEN
      expect(businessUnitFormService.getBusinessUnit).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(businessUnitService.update).toHaveBeenCalledWith(expect.objectContaining(businessUnit));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessUnit>>();
      const businessUnit = { id: 123 };
      jest.spyOn(businessUnitFormService, 'getBusinessUnit').mockReturnValue({ id: null });
      jest.spyOn(businessUnitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessUnit: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessUnit }));
      saveSubject.complete();

      // THEN
      expect(businessUnitFormService.getBusinessUnit).toHaveBeenCalled();
      expect(businessUnitService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessUnit>>();
      const businessUnit = { id: 123 };
      jest.spyOn(businessUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(businessUnitService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrganization', () => {
      it('Should forward to organizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(organizationService, 'compareOrganization');
        comp.compareOrganization(entity, entity2);
        expect(organizationService.compareOrganization).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBusinessUnit', () => {
      it('Should forward to businessUnitService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(businessUnitService, 'compareBusinessUnit');
        comp.compareBusinessUnit(entity, entity2);
        expect(businessUnitService.compareBusinessUnit).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
