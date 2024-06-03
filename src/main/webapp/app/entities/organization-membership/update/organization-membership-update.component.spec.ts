import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IOrganizationMembership } from '../organization-membership.model';
import { OrganizationMembershipService } from '../service/organization-membership.service';
import { OrganizationMembershipFormService } from './organization-membership-form.service';

import { OrganizationMembershipUpdateComponent } from './organization-membership-update.component';

describe('OrganizationMembership Management Update Component', () => {
  let comp: OrganizationMembershipUpdateComponent;
  let fixture: ComponentFixture<OrganizationMembershipUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organizationMembershipFormService: OrganizationMembershipFormService;
  let organizationMembershipService: OrganizationMembershipService;
  let organizationService: OrganizationService;
  let personService: PersonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrganizationMembershipUpdateComponent],
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
      .overrideTemplate(OrganizationMembershipUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationMembershipUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organizationMembershipFormService = TestBed.inject(OrganizationMembershipFormService);
    organizationMembershipService = TestBed.inject(OrganizationMembershipService);
    organizationService = TestBed.inject(OrganizationService);
    personService = TestBed.inject(PersonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Organization query and add missing value', () => {
      const organizationMembership: IOrganizationMembership = { id: 456 };
      const organization: IOrganization = { id: 10551 };
      organizationMembership.organization = organization;

      const organizationCollection: IOrganization[] = [{ id: 8728 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [organization];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organizationMembership });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining),
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Person query and add missing value', () => {
      const organizationMembership: IOrganizationMembership = { id: 456 };
      const person: IPerson = { id: 2398 };
      organizationMembership.person = person;

      const personCollection: IPerson[] = [{ id: 12307 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const additionalPeople = [person];
      const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organizationMembership });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(
        personCollection,
        ...additionalPeople.map(expect.objectContaining),
      );
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const organizationMembership: IOrganizationMembership = { id: 456 };
      const organization: IOrganization = { id: 11721 };
      organizationMembership.organization = organization;
      const person: IPerson = { id: 30870 };
      organizationMembership.person = person;

      activatedRoute.data = of({ organizationMembership });
      comp.ngOnInit();

      expect(comp.organizationsSharedCollection).toContain(organization);
      expect(comp.peopleSharedCollection).toContain(person);
      expect(comp.organizationMembership).toEqual(organizationMembership);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationMembership>>();
      const organizationMembership = { id: 123 };
      jest.spyOn(organizationMembershipFormService, 'getOrganizationMembership').mockReturnValue(organizationMembership);
      jest.spyOn(organizationMembershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationMembership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationMembership }));
      saveSubject.complete();

      // THEN
      expect(organizationMembershipFormService.getOrganizationMembership).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organizationMembershipService.update).toHaveBeenCalledWith(expect.objectContaining(organizationMembership));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationMembership>>();
      const organizationMembership = { id: 123 };
      jest.spyOn(organizationMembershipFormService, 'getOrganizationMembership').mockReturnValue({ id: null });
      jest.spyOn(organizationMembershipService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationMembership: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationMembership }));
      saveSubject.complete();

      // THEN
      expect(organizationMembershipFormService.getOrganizationMembership).toHaveBeenCalled();
      expect(organizationMembershipService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationMembership>>();
      const organizationMembership = { id: 123 };
      jest.spyOn(organizationMembershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationMembership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organizationMembershipService.update).toHaveBeenCalled();
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

    describe('comparePerson', () => {
      it('Should forward to personService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personService, 'comparePerson');
        comp.comparePerson(entity, entity2);
        expect(personService.comparePerson).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
