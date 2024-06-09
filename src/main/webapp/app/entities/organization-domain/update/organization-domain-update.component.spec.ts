import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { OrganizationDomainService } from '../service/organization-domain.service';
import { IOrganizationDomain } from '../organization-domain.model';
import { OrganizationDomainFormService } from './organization-domain-form.service';

import { OrganizationDomainUpdateComponent } from './organization-domain-update.component';

describe('OrganizationDomain Management Update Component', () => {
  let comp: OrganizationDomainUpdateComponent;
  let fixture: ComponentFixture<OrganizationDomainUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organizationDomainFormService: OrganizationDomainFormService;
  let organizationDomainService: OrganizationDomainService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrganizationDomainUpdateComponent],
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
      .overrideTemplate(OrganizationDomainUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationDomainUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organizationDomainFormService = TestBed.inject(OrganizationDomainFormService);
    organizationDomainService = TestBed.inject(OrganizationDomainService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Organization query and add missing value', () => {
      const organizationDomain: IOrganizationDomain = { id: 456 };
      const organization: IOrganization = { id: 2872 };
      organizationDomain.organization = organization;

      const organizationCollection: IOrganization[] = [{ id: 17884 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [organization];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organizationDomain });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining),
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const organizationDomain: IOrganizationDomain = { id: 456 };
      const organization: IOrganization = { id: 15225 };
      organizationDomain.organization = organization;

      activatedRoute.data = of({ organizationDomain });
      comp.ngOnInit();

      expect(comp.organizationsSharedCollection).toContain(organization);
      expect(comp.organizationDomain).toEqual(organizationDomain);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationDomain>>();
      const organizationDomain = { id: 123 };
      jest.spyOn(organizationDomainFormService, 'getOrganizationDomain').mockReturnValue(organizationDomain);
      jest.spyOn(organizationDomainService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationDomain });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationDomain }));
      saveSubject.complete();

      // THEN
      expect(organizationDomainFormService.getOrganizationDomain).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organizationDomainService.update).toHaveBeenCalledWith(expect.objectContaining(organizationDomain));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationDomain>>();
      const organizationDomain = { id: 123 };
      jest.spyOn(organizationDomainFormService, 'getOrganizationDomain').mockReturnValue({ id: null });
      jest.spyOn(organizationDomainService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationDomain: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationDomain }));
      saveSubject.complete();

      // THEN
      expect(organizationDomainFormService.getOrganizationDomain).toHaveBeenCalled();
      expect(organizationDomainService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationDomain>>();
      const organizationDomain = { id: 123 };
      jest.spyOn(organizationDomainService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationDomain });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organizationDomainService.update).toHaveBeenCalled();
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
