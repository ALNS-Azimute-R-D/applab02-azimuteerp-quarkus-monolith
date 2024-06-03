import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITownCity } from 'app/entities/town-city/town-city.model';
import { TownCityService } from 'app/entities/town-city/service/town-city.service';
import { DistrictService } from '../service/district.service';
import { IDistrict } from '../district.model';
import { DistrictFormService } from './district-form.service';

import { DistrictUpdateComponent } from './district-update.component';

describe('District Management Update Component', () => {
  let comp: DistrictUpdateComponent;
  let fixture: ComponentFixture<DistrictUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let districtFormService: DistrictFormService;
  let districtService: DistrictService;
  let townCityService: TownCityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DistrictUpdateComponent],
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
      .overrideTemplate(DistrictUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DistrictUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    districtFormService = TestBed.inject(DistrictFormService);
    districtService = TestBed.inject(DistrictService);
    townCityService = TestBed.inject(TownCityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TownCity query and add missing value', () => {
      const district: IDistrict = { id: 456 };
      const townCity: ITownCity = { id: 7616 };
      district.townCity = townCity;

      const townCityCollection: ITownCity[] = [{ id: 11351 }];
      jest.spyOn(townCityService, 'query').mockReturnValue(of(new HttpResponse({ body: townCityCollection })));
      const additionalTownCities = [townCity];
      const expectedCollection: ITownCity[] = [...additionalTownCities, ...townCityCollection];
      jest.spyOn(townCityService, 'addTownCityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ district });
      comp.ngOnInit();

      expect(townCityService.query).toHaveBeenCalled();
      expect(townCityService.addTownCityToCollectionIfMissing).toHaveBeenCalledWith(
        townCityCollection,
        ...additionalTownCities.map(expect.objectContaining),
      );
      expect(comp.townCitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const district: IDistrict = { id: 456 };
      const townCity: ITownCity = { id: 12245 };
      district.townCity = townCity;

      activatedRoute.data = of({ district });
      comp.ngOnInit();

      expect(comp.townCitiesSharedCollection).toContain(townCity);
      expect(comp.district).toEqual(district);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtFormService, 'getDistrict').mockReturnValue(district);
      jest.spyOn(districtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: district }));
      saveSubject.complete();

      // THEN
      expect(districtFormService.getDistrict).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(districtService.update).toHaveBeenCalledWith(expect.objectContaining(district));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtFormService, 'getDistrict').mockReturnValue({ id: null });
      jest.spyOn(districtService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: district }));
      saveSubject.complete();

      // THEN
      expect(districtFormService.getDistrict).toHaveBeenCalled();
      expect(districtService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDistrict>>();
      const district = { id: 123 };
      jest.spyOn(districtService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ district });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(districtService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTownCity', () => {
      it('Should forward to townCityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(townCityService, 'compareTownCity');
        comp.compareTownCity(entity, entity2);
        expect(townCityService.compareTownCity).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
