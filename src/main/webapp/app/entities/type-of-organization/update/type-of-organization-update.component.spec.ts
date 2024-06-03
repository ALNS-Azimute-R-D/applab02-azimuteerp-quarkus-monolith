import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeOfOrganizationService } from '../service/type-of-organization.service';
import { ITypeOfOrganization } from '../type-of-organization.model';
import { TypeOfOrganizationFormService } from './type-of-organization-form.service';

import { TypeOfOrganizationUpdateComponent } from './type-of-organization-update.component';

describe('TypeOfOrganization Management Update Component', () => {
  let comp: TypeOfOrganizationUpdateComponent;
  let fixture: ComponentFixture<TypeOfOrganizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeOfOrganizationFormService: TypeOfOrganizationFormService;
  let typeOfOrganizationService: TypeOfOrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeOfOrganizationUpdateComponent],
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
      .overrideTemplate(TypeOfOrganizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeOfOrganizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeOfOrganizationFormService = TestBed.inject(TypeOfOrganizationFormService);
    typeOfOrganizationService = TestBed.inject(TypeOfOrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeOfOrganization: ITypeOfOrganization = { id: 456 };

      activatedRoute.data = of({ typeOfOrganization });
      comp.ngOnInit();

      expect(comp.typeOfOrganization).toEqual(typeOfOrganization);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfOrganization>>();
      const typeOfOrganization = { id: 123 };
      jest.spyOn(typeOfOrganizationFormService, 'getTypeOfOrganization').mockReturnValue(typeOfOrganization);
      jest.spyOn(typeOfOrganizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfOrganization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeOfOrganization }));
      saveSubject.complete();

      // THEN
      expect(typeOfOrganizationFormService.getTypeOfOrganization).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeOfOrganizationService.update).toHaveBeenCalledWith(expect.objectContaining(typeOfOrganization));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfOrganization>>();
      const typeOfOrganization = { id: 123 };
      jest.spyOn(typeOfOrganizationFormService, 'getTypeOfOrganization').mockReturnValue({ id: null });
      jest.spyOn(typeOfOrganizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfOrganization: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeOfOrganization }));
      saveSubject.complete();

      // THEN
      expect(typeOfOrganizationFormService.getTypeOfOrganization).toHaveBeenCalled();
      expect(typeOfOrganizationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfOrganization>>();
      const typeOfOrganization = { id: 123 };
      jest.spyOn(typeOfOrganizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfOrganization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeOfOrganizationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
