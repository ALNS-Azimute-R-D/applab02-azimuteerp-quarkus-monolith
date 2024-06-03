import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { LocalityService } from '../service/locality.service';
import { ILocality } from '../locality.model';
import { LocalityFormService } from './locality-form.service';

import { LocalityUpdateComponent } from './locality-update.component';

describe('Locality Management Update Component', () => {
  let comp: LocalityUpdateComponent;
  let fixture: ComponentFixture<LocalityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let localityFormService: LocalityFormService;
  let localityService: LocalityService;
  let countryService: CountryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LocalityUpdateComponent],
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
      .overrideTemplate(LocalityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocalityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    localityFormService = TestBed.inject(LocalityFormService);
    localityService = TestBed.inject(LocalityService);
    countryService = TestBed.inject(CountryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Country query and add missing value', () => {
      const locality: ILocality = { id: 456 };
      const country: ICountry = { id: 12057 };
      locality.country = country;

      const countryCollection: ICountry[] = [{ id: 3341 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ locality });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const locality: ILocality = { id: 456 };
      const country: ICountry = { id: 7717 };
      locality.country = country;

      activatedRoute.data = of({ locality });
      comp.ngOnInit();

      expect(comp.countriesSharedCollection).toContain(country);
      expect(comp.locality).toEqual(locality);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocality>>();
      const locality = { id: 123 };
      jest.spyOn(localityFormService, 'getLocality').mockReturnValue(locality);
      jest.spyOn(localityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locality }));
      saveSubject.complete();

      // THEN
      expect(localityFormService.getLocality).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(localityService.update).toHaveBeenCalledWith(expect.objectContaining(locality));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocality>>();
      const locality = { id: 123 };
      jest.spyOn(localityFormService, 'getLocality').mockReturnValue({ id: null });
      jest.spyOn(localityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locality: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locality }));
      saveSubject.complete();

      // THEN
      expect(localityFormService.getLocality).toHaveBeenCalled();
      expect(localityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocality>>();
      const locality = { id: 123 };
      jest.spyOn(localityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(localityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCountry', () => {
      it('Should forward to countryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(countryService, 'compareCountry');
        comp.compareCountry(entity, entity2);
        expect(countryService.compareCountry).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
