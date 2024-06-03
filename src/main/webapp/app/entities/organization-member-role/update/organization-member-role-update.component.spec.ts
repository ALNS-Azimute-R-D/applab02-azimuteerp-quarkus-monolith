import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IOrganizationMembership } from 'app/entities/organization-membership/organization-membership.model';
import { OrganizationMembershipService } from 'app/entities/organization-membership/service/organization-membership.service';
import { IOrganizationRole } from 'app/entities/organization-role/organization-role.model';
import { OrganizationRoleService } from 'app/entities/organization-role/service/organization-role.service';
import { IOrganizationMemberRole } from '../organization-member-role.model';
import { OrganizationMemberRoleService } from '../service/organization-member-role.service';
import { OrganizationMemberRoleFormService } from './organization-member-role-form.service';

import { OrganizationMemberRoleUpdateComponent } from './organization-member-role-update.component';

describe('OrganizationMemberRole Management Update Component', () => {
  let comp: OrganizationMemberRoleUpdateComponent;
  let fixture: ComponentFixture<OrganizationMemberRoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organizationMemberRoleFormService: OrganizationMemberRoleFormService;
  let organizationMemberRoleService: OrganizationMemberRoleService;
  let organizationMembershipService: OrganizationMembershipService;
  let organizationRoleService: OrganizationRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrganizationMemberRoleUpdateComponent],
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
      .overrideTemplate(OrganizationMemberRoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationMemberRoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organizationMemberRoleFormService = TestBed.inject(OrganizationMemberRoleFormService);
    organizationMemberRoleService = TestBed.inject(OrganizationMemberRoleService);
    organizationMembershipService = TestBed.inject(OrganizationMembershipService);
    organizationRoleService = TestBed.inject(OrganizationRoleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call OrganizationMembership query and add missing value', () => {
      const organizationMemberRole: IOrganizationMemberRole = { id: 456 };
      const organizationMembership: IOrganizationMembership = { id: 354 };
      organizationMemberRole.organizationMembership = organizationMembership;

      const organizationMembershipCollection: IOrganizationMembership[] = [{ id: 8117 }];
      jest.spyOn(organizationMembershipService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationMembershipCollection })));
      const additionalOrganizationMemberships = [organizationMembership];
      const expectedCollection: IOrganizationMembership[] = [...additionalOrganizationMemberships, ...organizationMembershipCollection];
      jest.spyOn(organizationMembershipService, 'addOrganizationMembershipToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organizationMemberRole });
      comp.ngOnInit();

      expect(organizationMembershipService.query).toHaveBeenCalled();
      expect(organizationMembershipService.addOrganizationMembershipToCollectionIfMissing).toHaveBeenCalledWith(
        organizationMembershipCollection,
        ...additionalOrganizationMemberships.map(expect.objectContaining),
      );
      expect(comp.organizationMembershipsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrganizationRole query and add missing value', () => {
      const organizationMemberRole: IOrganizationMemberRole = { id: 456 };
      const organizationRole: IOrganizationRole = { id: 13384 };
      organizationMemberRole.organizationRole = organizationRole;

      const organizationRoleCollection: IOrganizationRole[] = [{ id: 5931 }];
      jest.spyOn(organizationRoleService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationRoleCollection })));
      const additionalOrganizationRoles = [organizationRole];
      const expectedCollection: IOrganizationRole[] = [...additionalOrganizationRoles, ...organizationRoleCollection];
      jest.spyOn(organizationRoleService, 'addOrganizationRoleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organizationMemberRole });
      comp.ngOnInit();

      expect(organizationRoleService.query).toHaveBeenCalled();
      expect(organizationRoleService.addOrganizationRoleToCollectionIfMissing).toHaveBeenCalledWith(
        organizationRoleCollection,
        ...additionalOrganizationRoles.map(expect.objectContaining),
      );
      expect(comp.organizationRolesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const organizationMemberRole: IOrganizationMemberRole = { id: 456 };
      const organizationMembership: IOrganizationMembership = { id: 7666 };
      organizationMemberRole.organizationMembership = organizationMembership;
      const organizationRole: IOrganizationRole = { id: 19381 };
      organizationMemberRole.organizationRole = organizationRole;

      activatedRoute.data = of({ organizationMemberRole });
      comp.ngOnInit();

      expect(comp.organizationMembershipsSharedCollection).toContain(organizationMembership);
      expect(comp.organizationRolesSharedCollection).toContain(organizationRole);
      expect(comp.organizationMemberRole).toEqual(organizationMemberRole);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationMemberRole>>();
      const organizationMemberRole = { id: 123 };
      jest.spyOn(organizationMemberRoleFormService, 'getOrganizationMemberRole').mockReturnValue(organizationMemberRole);
      jest.spyOn(organizationMemberRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationMemberRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationMemberRole }));
      saveSubject.complete();

      // THEN
      expect(organizationMemberRoleFormService.getOrganizationMemberRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organizationMemberRoleService.update).toHaveBeenCalledWith(expect.objectContaining(organizationMemberRole));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationMemberRole>>();
      const organizationMemberRole = { id: 123 };
      jest.spyOn(organizationMemberRoleFormService, 'getOrganizationMemberRole').mockReturnValue({ id: null });
      jest.spyOn(organizationMemberRoleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationMemberRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationMemberRole }));
      saveSubject.complete();

      // THEN
      expect(organizationMemberRoleFormService.getOrganizationMemberRole).toHaveBeenCalled();
      expect(organizationMemberRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationMemberRole>>();
      const organizationMemberRole = { id: 123 };
      jest.spyOn(organizationMemberRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationMemberRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organizationMemberRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrganizationMembership', () => {
      it('Should forward to organizationMembershipService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(organizationMembershipService, 'compareOrganizationMembership');
        comp.compareOrganizationMembership(entity, entity2);
        expect(organizationMembershipService.compareOrganizationMembership).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrganizationRole', () => {
      it('Should forward to organizationRoleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(organizationRoleService, 'compareOrganizationRole');
        comp.compareOrganizationRole(entity, entity2);
        expect(organizationRoleService.compareOrganizationRole).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
