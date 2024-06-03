import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';
import { TownCityService } from '../service/town-city.service';
import { ITownCity } from '../town-city.model';
import { TownCityFormService } from './town-city-form.service';

import { TownCityUpdateComponent } from './town-city-update.component';

describe('TownCity Management Update Component', () => {
  let comp: TownCityUpdateComponent;
  let fixture: ComponentFixture<TownCityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let townCityFormService: TownCityFormService;
  let townCityService: TownCityService;
  let provinceService: ProvinceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TownCityUpdateComponent],
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
      .overrideTemplate(TownCityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TownCityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    townCityFormService = TestBed.inject(TownCityFormService);
    townCityService = TestBed.inject(TownCityService);
    provinceService = TestBed.inject(ProvinceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Province query and add missing value', () => {
      const townCity: ITownCity = { id: 456 };
      const province: IProvince = { id: 10714 };
      townCity.province = province;

      const provinceCollection: IProvince[] = [{ id: 26870 }];
      jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
      const additionalProvinces = [province];
      const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
      jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ townCity });
      comp.ngOnInit();

      expect(provinceService.query).toHaveBeenCalled();
      expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(
        provinceCollection,
        ...additionalProvinces.map(expect.objectContaining),
      );
      expect(comp.provincesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const townCity: ITownCity = { id: 456 };
      const province: IProvince = { id: 27937 };
      townCity.province = province;

      activatedRoute.data = of({ townCity });
      comp.ngOnInit();

      expect(comp.provincesSharedCollection).toContain(province);
      expect(comp.townCity).toEqual(townCity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITownCity>>();
      const townCity = { id: 123 };
      jest.spyOn(townCityFormService, 'getTownCity').mockReturnValue(townCity);
      jest.spyOn(townCityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ townCity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: townCity }));
      saveSubject.complete();

      // THEN
      expect(townCityFormService.getTownCity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(townCityService.update).toHaveBeenCalledWith(expect.objectContaining(townCity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITownCity>>();
      const townCity = { id: 123 };
      jest.spyOn(townCityFormService, 'getTownCity').mockReturnValue({ id: null });
      jest.spyOn(townCityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ townCity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: townCity }));
      saveSubject.complete();

      // THEN
      expect(townCityFormService.getTownCity).toHaveBeenCalled();
      expect(townCityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITownCity>>();
      const townCity = { id: 123 };
      jest.spyOn(townCityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ townCity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(townCityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProvince', () => {
      it('Should forward to provinceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(provinceService, 'compareProvince');
        comp.compareProvince(entity, entity2);
        expect(provinceService.compareProvince).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
