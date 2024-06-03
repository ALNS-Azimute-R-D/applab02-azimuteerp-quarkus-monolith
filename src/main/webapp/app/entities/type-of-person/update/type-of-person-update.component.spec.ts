import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypeOfPersonService } from '../service/type-of-person.service';
import { ITypeOfPerson } from '../type-of-person.model';
import { TypeOfPersonFormService } from './type-of-person-form.service';

import { TypeOfPersonUpdateComponent } from './type-of-person-update.component';

describe('TypeOfPerson Management Update Component', () => {
  let comp: TypeOfPersonUpdateComponent;
  let fixture: ComponentFixture<TypeOfPersonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeOfPersonFormService: TypeOfPersonFormService;
  let typeOfPersonService: TypeOfPersonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeOfPersonUpdateComponent],
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
      .overrideTemplate(TypeOfPersonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeOfPersonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeOfPersonFormService = TestBed.inject(TypeOfPersonFormService);
    typeOfPersonService = TestBed.inject(TypeOfPersonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const typeOfPerson: ITypeOfPerson = { id: 456 };

      activatedRoute.data = of({ typeOfPerson });
      comp.ngOnInit();

      expect(comp.typeOfPerson).toEqual(typeOfPerson);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfPerson>>();
      const typeOfPerson = { id: 123 };
      jest.spyOn(typeOfPersonFormService, 'getTypeOfPerson').mockReturnValue(typeOfPerson);
      jest.spyOn(typeOfPersonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfPerson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeOfPerson }));
      saveSubject.complete();

      // THEN
      expect(typeOfPersonFormService.getTypeOfPerson).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeOfPersonService.update).toHaveBeenCalledWith(expect.objectContaining(typeOfPerson));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfPerson>>();
      const typeOfPerson = { id: 123 };
      jest.spyOn(typeOfPersonFormService, 'getTypeOfPerson').mockReturnValue({ id: null });
      jest.spyOn(typeOfPersonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfPerson: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeOfPerson }));
      saveSubject.complete();

      // THEN
      expect(typeOfPersonFormService.getTypeOfPerson).toHaveBeenCalled();
      expect(typeOfPersonService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfPerson>>();
      const typeOfPerson = { id: 123 };
      jest.spyOn(typeOfPersonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfPerson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeOfPersonService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
