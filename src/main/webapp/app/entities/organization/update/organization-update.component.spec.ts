import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';
import { ITypeOfOrganization } from 'app/entities/type-of-organization/type-of-organization.model';
import { TypeOfOrganizationService } from 'app/entities/type-of-organization/service/type-of-organization.service';
import { IOrganization } from '../organization.model';
import { OrganizationService } from '../service/organization.service';
import { OrganizationFormService } from './organization-form.service';

import { OrganizationUpdateComponent } from './organization-update.component';

describe('Organization Management Update Component', () => {
  let comp: OrganizationUpdateComponent;
  let fixture: ComponentFixture<OrganizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organizationFormService: OrganizationFormService;
  let organizationService: OrganizationService;
  let tenantService: TenantService;
  let typeOfOrganizationService: TypeOfOrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrganizationUpdateComponent],
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
      .overrideTemplate(OrganizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organizationFormService = TestBed.inject(OrganizationFormService);
    organizationService = TestBed.inject(OrganizationService);
    tenantService = TestBed.inject(TenantService);
    typeOfOrganizationService = TestBed.inject(TypeOfOrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tenant query and add missing value', () => {
      const organization: IOrganization = { id: 456 };
      const tenant: ITenant = { id: 1992 };
      organization.tenant = tenant;

      const tenantCollection: ITenant[] = [{ id: 26094 }];
      jest.spyOn(tenantService, 'query').mockReturnValue(of(new HttpResponse({ body: tenantCollection })));
      const additionalTenants = [tenant];
      const expectedCollection: ITenant[] = [...additionalTenants, ...tenantCollection];
      jest.spyOn(tenantService, 'addTenantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      expect(tenantService.query).toHaveBeenCalled();
      expect(tenantService.addTenantToCollectionIfMissing).toHaveBeenCalledWith(
        tenantCollection,
        ...additionalTenants.map(expect.objectContaining),
      );
      expect(comp.tenantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypeOfOrganization query and add missing value', () => {
      const organization: IOrganization = { id: 456 };
      const typeOfOrganization: ITypeOfOrganization = { id: 6771 };
      organization.typeOfOrganization = typeOfOrganization;

      const typeOfOrganizationCollection: ITypeOfOrganization[] = [{ id: 23858 }];
      jest.spyOn(typeOfOrganizationService, 'query').mockReturnValue(of(new HttpResponse({ body: typeOfOrganizationCollection })));
      const additionalTypeOfOrganizations = [typeOfOrganization];
      const expectedCollection: ITypeOfOrganization[] = [...additionalTypeOfOrganizations, ...typeOfOrganizationCollection];
      jest.spyOn(typeOfOrganizationService, 'addTypeOfOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      expect(typeOfOrganizationService.query).toHaveBeenCalled();
      expect(typeOfOrganizationService.addTypeOfOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        typeOfOrganizationCollection,
        ...additionalTypeOfOrganizations.map(expect.objectContaining),
      );
      expect(comp.typeOfOrganizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Organization query and add missing value', () => {
      const organization: IOrganization = { id: 456 };
      const organizationParent: IOrganization = { id: 4894 };
      organization.organizationParent = organizationParent;

      const organizationCollection: IOrganization[] = [{ id: 27971 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [organizationParent];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining),
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const organization: IOrganization = { id: 456 };
      const tenant: ITenant = { id: 14919 };
      organization.tenant = tenant;
      const typeOfOrganization: ITypeOfOrganization = { id: 30159 };
      organization.typeOfOrganization = typeOfOrganization;
      const organizationParent: IOrganization = { id: 8909 };
      organization.organizationParent = organizationParent;

      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      expect(comp.tenantsSharedCollection).toContain(tenant);
      expect(comp.typeOfOrganizationsSharedCollection).toContain(typeOfOrganization);
      expect(comp.organizationsSharedCollection).toContain(organizationParent);
      expect(comp.organization).toEqual(organization);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganization>>();
      const organization = { id: 123 };
      jest.spyOn(organizationFormService, 'getOrganization').mockReturnValue(organization);
      jest.spyOn(organizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organization }));
      saveSubject.complete();

      // THEN
      expect(organizationFormService.getOrganization).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organizationService.update).toHaveBeenCalledWith(expect.objectContaining(organization));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganization>>();
      const organization = { id: 123 };
      jest.spyOn(organizationFormService, 'getOrganization').mockReturnValue({ id: null });
      jest.spyOn(organizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organization: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organization }));
      saveSubject.complete();

      // THEN
      expect(organizationFormService.getOrganization).toHaveBeenCalled();
      expect(organizationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganization>>();
      const organization = { id: 123 };
      jest.spyOn(organizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organizationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTenant', () => {
      it('Should forward to tenantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tenantService, 'compareTenant');
        comp.compareTenant(entity, entity2);
        expect(tenantService.compareTenant).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeOfOrganization', () => {
      it('Should forward to typeOfOrganizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeOfOrganizationService, 'compareTypeOfOrganization');
        comp.compareTypeOfOrganization(entity, entity2);
        expect(typeOfOrganizationService.compareTypeOfOrganization).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrganization', () => {
      it('Should forward to organizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(organizationService, 'compareOrganization');
        comp.compareOrganization(entity, entity2);
        expect(organizationService.compareOrganization).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
