import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { OrganizationRoleService } from '../service/organization-role.service';
import { IOrganizationRole } from '../organization-role.model';
import { OrganizationRoleFormService } from './organization-role-form.service';

import { OrganizationRoleUpdateComponent } from './organization-role-update.component';

describe('OrganizationRole Management Update Component', () => {
  let comp: OrganizationRoleUpdateComponent;
  let fixture: ComponentFixture<OrganizationRoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organizationRoleFormService: OrganizationRoleFormService;
  let organizationRoleService: OrganizationRoleService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrganizationRoleUpdateComponent],
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
      .overrideTemplate(OrganizationRoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationRoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organizationRoleFormService = TestBed.inject(OrganizationRoleFormService);
    organizationRoleService = TestBed.inject(OrganizationRoleService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Organization query and add missing value', () => {
      const organizationRole: IOrganizationRole = { id: 456 };
      const organization: IOrganization = { id: 17201 };
      organizationRole.organization = organization;

      const organizationCollection: IOrganization[] = [{ id: 23397 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [organization];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organizationRole });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining),
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const organizationRole: IOrganizationRole = { id: 456 };
      const organization: IOrganization = { id: 6221 };
      organizationRole.organization = organization;

      activatedRoute.data = of({ organizationRole });
      comp.ngOnInit();

      expect(comp.organizationsSharedCollection).toContain(organization);
      expect(comp.organizationRole).toEqual(organizationRole);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationRole>>();
      const organizationRole = { id: 123 };
      jest.spyOn(organizationRoleFormService, 'getOrganizationRole').mockReturnValue(organizationRole);
      jest.spyOn(organizationRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationRole }));
      saveSubject.complete();

      // THEN
      expect(organizationRoleFormService.getOrganizationRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organizationRoleService.update).toHaveBeenCalledWith(expect.objectContaining(organizationRole));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationRole>>();
      const organizationRole = { id: 123 };
      jest.spyOn(organizationRoleFormService, 'getOrganizationRole').mockReturnValue({ id: null });
      jest.spyOn(organizationRoleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationRole }));
      saveSubject.complete();

      // THEN
      expect(organizationRoleFormService.getOrganizationRole).toHaveBeenCalled();
      expect(organizationRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationRole>>();
      const organizationRole = { id: 123 };
      jest.spyOn(organizationRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organizationRoleService.update).toHaveBeenCalled();
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
  });
});
