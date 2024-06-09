import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { OrganizationAttributeService } from '../service/organization-attribute.service';
import { IOrganizationAttribute } from '../organization-attribute.model';
import { OrganizationAttributeFormService } from './organization-attribute-form.service';

import { OrganizationAttributeUpdateComponent } from './organization-attribute-update.component';

describe('OrganizationAttribute Management Update Component', () => {
  let comp: OrganizationAttributeUpdateComponent;
  let fixture: ComponentFixture<OrganizationAttributeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organizationAttributeFormService: OrganizationAttributeFormService;
  let organizationAttributeService: OrganizationAttributeService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrganizationAttributeUpdateComponent],
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
      .overrideTemplate(OrganizationAttributeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganizationAttributeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organizationAttributeFormService = TestBed.inject(OrganizationAttributeFormService);
    organizationAttributeService = TestBed.inject(OrganizationAttributeService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Organization query and add missing value', () => {
      const organizationAttribute: IOrganizationAttribute = { id: 456 };
      const organization: IOrganization = { id: 4932 };
      organizationAttribute.organization = organization;

      const organizationCollection: IOrganization[] = [{ id: 21532 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [organization];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organizationAttribute });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining),
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const organizationAttribute: IOrganizationAttribute = { id: 456 };
      const organization: IOrganization = { id: 19548 };
      organizationAttribute.organization = organization;

      activatedRoute.data = of({ organizationAttribute });
      comp.ngOnInit();

      expect(comp.organizationsSharedCollection).toContain(organization);
      expect(comp.organizationAttribute).toEqual(organizationAttribute);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationAttribute>>();
      const organizationAttribute = { id: 123 };
      jest.spyOn(organizationAttributeFormService, 'getOrganizationAttribute').mockReturnValue(organizationAttribute);
      jest.spyOn(organizationAttributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationAttribute }));
      saveSubject.complete();

      // THEN
      expect(organizationAttributeFormService.getOrganizationAttribute).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organizationAttributeService.update).toHaveBeenCalledWith(expect.objectContaining(organizationAttribute));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationAttribute>>();
      const organizationAttribute = { id: 123 };
      jest.spyOn(organizationAttributeFormService, 'getOrganizationAttribute').mockReturnValue({ id: null });
      jest.spyOn(organizationAttributeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationAttribute: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organizationAttribute }));
      saveSubject.complete();

      // THEN
      expect(organizationAttributeFormService.getOrganizationAttribute).toHaveBeenCalled();
      expect(organizationAttributeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganizationAttribute>>();
      const organizationAttribute = { id: 123 };
      jest.spyOn(organizationAttributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organizationAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organizationAttributeService.update).toHaveBeenCalled();
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
