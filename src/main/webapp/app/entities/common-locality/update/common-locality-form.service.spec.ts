import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../common-locality.test-samples';

import { CommonLocalityFormService } from './common-locality-form.service';

describe('CommonLocality Form Service', () => {
  let service: CommonLocalityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommonLocalityFormService);
  });

  describe('Service methods', () => {
    describe('createCommonLocalityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCommonLocalityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            streetAddress: expect.any(Object),
            houseNumber: expect.any(Object),
            locationName: expect.any(Object),
            postalCode: expect.any(Object),
            geoPolygonArea: expect.any(Object),
            district: expect.any(Object),
          }),
        );
      });

      it('passing ICommonLocality should create a new form with FormGroup', () => {
        const formGroup = service.createCommonLocalityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            streetAddress: expect.any(Object),
            houseNumber: expect.any(Object),
            locationName: expect.any(Object),
            postalCode: expect.any(Object),
            geoPolygonArea: expect.any(Object),
            district: expect.any(Object),
          }),
        );
      });
    });

    describe('getCommonLocality', () => {
      it('should return NewCommonLocality for default CommonLocality initial value', () => {
        const formGroup = service.createCommonLocalityFormGroup(sampleWithNewData);

        const commonLocality = service.getCommonLocality(formGroup) as any;

        expect(commonLocality).toMatchObject(sampleWithNewData);
      });

      it('should return NewCommonLocality for empty CommonLocality initial value', () => {
        const formGroup = service.createCommonLocalityFormGroup();

        const commonLocality = service.getCommonLocality(formGroup) as any;

        expect(commonLocality).toMatchObject({});
      });

      it('should return ICommonLocality', () => {
        const formGroup = service.createCommonLocalityFormGroup(sampleWithRequiredData);

        const commonLocality = service.getCommonLocality(formGroup) as any;

        expect(commonLocality).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICommonLocality should not enable id FormControl', () => {
        const formGroup = service.createCommonLocalityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCommonLocality should disable id FormControl', () => {
        const formGroup = service.createCommonLocalityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
