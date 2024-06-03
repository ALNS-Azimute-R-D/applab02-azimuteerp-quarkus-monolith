import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../locality.test-samples';

import { LocalityFormService } from './locality-form.service';

describe('Locality Form Service', () => {
  let service: LocalityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocalityFormService);
  });

  describe('Service methods', () => {
    describe('createLocalityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLocalityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            geoPolygonArea: expect.any(Object),
            country: expect.any(Object),
          }),
        );
      });

      it('passing ILocality should create a new form with FormGroup', () => {
        const formGroup = service.createLocalityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            geoPolygonArea: expect.any(Object),
            country: expect.any(Object),
          }),
        );
      });
    });

    describe('getLocality', () => {
      it('should return NewLocality for default Locality initial value', () => {
        const formGroup = service.createLocalityFormGroup(sampleWithNewData);

        const locality = service.getLocality(formGroup) as any;

        expect(locality).toMatchObject(sampleWithNewData);
      });

      it('should return NewLocality for empty Locality initial value', () => {
        const formGroup = service.createLocalityFormGroup();

        const locality = service.getLocality(formGroup) as any;

        expect(locality).toMatchObject({});
      });

      it('should return ILocality', () => {
        const formGroup = service.createLocalityFormGroup(sampleWithRequiredData);

        const locality = service.getLocality(formGroup) as any;

        expect(locality).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILocality should not enable id FormControl', () => {
        const formGroup = service.createLocalityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLocality should disable id FormControl', () => {
        const formGroup = service.createLocalityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
