import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../town-city.test-samples';

import { TownCityFormService } from './town-city-form.service';

describe('TownCity Form Service', () => {
  let service: TownCityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TownCityFormService);
  });

  describe('Service methods', () => {
    describe('createTownCityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTownCityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            geoPolygonArea: expect.any(Object),
            province: expect.any(Object),
          }),
        );
      });

      it('passing ITownCity should create a new form with FormGroup', () => {
        const formGroup = service.createTownCityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            geoPolygonArea: expect.any(Object),
            province: expect.any(Object),
          }),
        );
      });
    });

    describe('getTownCity', () => {
      it('should return NewTownCity for default TownCity initial value', () => {
        const formGroup = service.createTownCityFormGroup(sampleWithNewData);

        const townCity = service.getTownCity(formGroup) as any;

        expect(townCity).toMatchObject(sampleWithNewData);
      });

      it('should return NewTownCity for empty TownCity initial value', () => {
        const formGroup = service.createTownCityFormGroup();

        const townCity = service.getTownCity(formGroup) as any;

        expect(townCity).toMatchObject({});
      });

      it('should return ITownCity', () => {
        const formGroup = service.createTownCityFormGroup(sampleWithRequiredData);

        const townCity = service.getTownCity(formGroup) as any;

        expect(townCity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITownCity should not enable id FormControl', () => {
        const formGroup = service.createTownCityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTownCity should disable id FormControl', () => {
        const formGroup = service.createTownCityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
