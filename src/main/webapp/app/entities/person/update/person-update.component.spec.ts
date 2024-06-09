import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITypeOfPerson } from 'app/entities/type-of-person/type-of-person.model';
import { TypeOfPersonService } from 'app/entities/type-of-person/service/type-of-person.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { IPerson } from '../person.model';
import { PersonService } from '../service/person.service';
import { PersonFormService } from './person-form.service';

import { PersonUpdateComponent } from './person-update.component';

describe('Person Management Update Component', () => {
  let comp: PersonUpdateComponent;
  let fixture: ComponentFixture<PersonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personFormService: PersonFormService;
  let personService: PersonService;
  let typeOfPersonService: TypeOfPersonService;
  let districtService: DistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PersonUpdateComponent],
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
      .overrideTemplate(PersonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personFormService = TestBed.inject(PersonFormService);
    personService = TestBed.inject(PersonService);
    typeOfPersonService = TestBed.inject(TypeOfPersonService);
    districtService = TestBed.inject(DistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TypeOfPerson query and add missing value', () => {
      const person: IPerson = { id: 456 };
      const typeOfPerson: ITypeOfPerson = { id: 16767 };
      person.typeOfPerson = typeOfPerson;

      const typeOfPersonCollection: ITypeOfPerson[] = [{ id: 26980 }];
      jest.spyOn(typeOfPersonService, 'query').mockReturnValue(of(new HttpResponse({ body: typeOfPersonCollection })));
      const additionalTypeOfPeople = [typeOfPerson];
      const expectedCollection: ITypeOfPerson[] = [...additionalTypeOfPeople, ...typeOfPersonCollection];
      jest.spyOn(typeOfPersonService, 'addTypeOfPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ person });
      comp.ngOnInit();

      expect(typeOfPersonService.query).toHaveBeenCalled();
      expect(typeOfPersonService.addTypeOfPersonToCollectionIfMissing).toHaveBeenCalledWith(
        typeOfPersonCollection,
        ...additionalTypeOfPeople.map(expect.objectContaining),
      );
      expect(comp.typeOfPeopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should call District query and add missing value', () => {
      const person: IPerson = { id: 456 };
      const district: IDistrict = { id: 1447 };
      person.district = district;

      const districtCollection: IDistrict[] = [{ id: 14680 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ person });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        districtCollection,
        ...additionalDistricts.map(expect.objectContaining),
      );
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Person query and add missing value', () => {
      const person: IPerson = { id: 456 };
      const managerPerson: IPerson = { id: 7972 };
      person.managerPerson = managerPerson;

      const personCollection: IPerson[] = [{ id: 17204 }];
      jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
      const additionalPeople = [managerPerson];
      const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
      jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ person });
      comp.ngOnInit();

      expect(personService.query).toHaveBeenCalled();
      expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(
        personCollection,
        ...additionalPeople.map(expect.objectContaining),
      );
      expect(comp.peopleSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const person: IPerson = { id: 456 };
      const typeOfPerson: ITypeOfPerson = { id: 27292 };
      person.typeOfPerson = typeOfPerson;
      const district: IDistrict = { id: 8674 };
      person.district = district;
      const managerPerson: IPerson = { id: 23917 };
      person.managerPerson = managerPerson;

      activatedRoute.data = of({ person });
      comp.ngOnInit();

      expect(comp.typeOfPeopleSharedCollection).toContain(typeOfPerson);
      expect(comp.districtsSharedCollection).toContain(district);
      expect(comp.peopleSharedCollection).toContain(managerPerson);
      expect(comp.person).toEqual(person);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerson>>();
      const person = { id: 123 };
      jest.spyOn(personFormService, 'getPerson').mockReturnValue(person);
      jest.spyOn(personService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ person });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: person }));
      saveSubject.complete();

      // THEN
      expect(personFormService.getPerson).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(personService.update).toHaveBeenCalledWith(expect.objectContaining(person));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerson>>();
      const person = { id: 123 };
      jest.spyOn(personFormService, 'getPerson').mockReturnValue({ id: null });
      jest.spyOn(personService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ person: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: person }));
      saveSubject.complete();

      // THEN
      expect(personFormService.getPerson).toHaveBeenCalled();
      expect(personService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerson>>();
      const person = { id: 123 };
      jest.spyOn(personService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ person });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeOfPerson', () => {
      it('Should forward to typeOfPersonService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeOfPersonService, 'compareTypeOfPerson');
        comp.compareTypeOfPerson(entity, entity2);
        expect(typeOfPersonService.compareTypeOfPerson).toHaveBeenCalledWith(entity, entity2);
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
