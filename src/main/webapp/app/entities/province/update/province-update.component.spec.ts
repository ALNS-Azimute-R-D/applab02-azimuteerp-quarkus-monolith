import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { ProvinceService } from '../service/province.service';
import { IProvince } from '../province.model';
import { ProvinceFormService } from './province-form.service';

import { ProvinceUpdateComponent } from './province-update.component';

describe('Province Management Update Component', () => {
  let comp: ProvinceUpdateComponent;
  let fixture: ComponentFixture<ProvinceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let provinceFormService: ProvinceFormService;
  let provinceService: ProvinceService;
  let countryService: CountryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProvinceUpdateComponent],
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
      .overrideTemplate(ProvinceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProvinceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    provinceFormService = TestBed.inject(ProvinceFormService);
    provinceService = TestBed.inject(ProvinceService);
    countryService = TestBed.inject(CountryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Country query and add missing value', () => {
      const province: IProvince = { id: 456 };
      const country: ICountry = { id: 27456 };
      province.country = country;

      const countryCollection: ICountry[] = [{ id: 30475 }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ province });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const province: IProvince = { id: 456 };
      const country: ICountry = { id: 25404 };
      province.country = country;

      activatedRoute.data = of({ province });
      comp.ngOnInit();

      expect(comp.countriesSharedCollection).toContain(country);
      expect(comp.province).toEqual(province);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvince>>();
      const province = { id: 123 };
      jest.spyOn(provinceFormService, 'getProvince').mockReturnValue(province);
      jest.spyOn(provinceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: province }));
      saveSubject.complete();

      // THEN
      expect(provinceFormService.getProvince).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(provinceService.update).toHaveBeenCalledWith(expect.objectContaining(province));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvince>>();
      const province = { id: 123 };
      jest.spyOn(provinceFormService, 'getProvince').mockReturnValue({ id: null });
      jest.spyOn(provinceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: province }));
      saveSubject.complete();

      // THEN
      expect(provinceFormService.getProvince).toHaveBeenCalled();
      expect(provinceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvince>>();
      const province = { id: 123 };
      jest.spyOn(provinceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(provinceService.update).toHaveBeenCalled();
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
