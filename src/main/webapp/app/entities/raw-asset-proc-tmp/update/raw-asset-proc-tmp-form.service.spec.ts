import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../raw-asset-proc-tmp.test-samples';

import { RawAssetProcTmpFormService } from './raw-asset-proc-tmp-form.service';

describe('RawAssetProcTmp Form Service', () => {
  let service: RawAssetProcTmpFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RawAssetProcTmpFormService);
  });

  describe('Service methods', () => {
    describe('createRawAssetProcTmpFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRawAssetProcTmpFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            statusRawProcessing: expect.any(Object),
            fullFilenamePath: expect.any(Object),
            assetRawContentAsBlob: expect.any(Object),
            customAttributesDetailsJSON: expect.any(Object),
            assetType: expect.any(Object),
          }),
        );
      });

      it('passing IRawAssetProcTmp should create a new form with FormGroup', () => {
        const formGroup = service.createRawAssetProcTmpFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            statusRawProcessing: expect.any(Object),
            fullFilenamePath: expect.any(Object),
            assetRawContentAsBlob: expect.any(Object),
            customAttributesDetailsJSON: expect.any(Object),
            assetType: expect.any(Object),
          }),
        );
      });
    });

    describe('getRawAssetProcTmp', () => {
      it('should return NewRawAssetProcTmp for default RawAssetProcTmp initial value', () => {
        const formGroup = service.createRawAssetProcTmpFormGroup(sampleWithNewData);

        const rawAssetProcTmp = service.getRawAssetProcTmp(formGroup) as any;

        expect(rawAssetProcTmp).toMatchObject(sampleWithNewData);
      });

      it('should return NewRawAssetProcTmp for empty RawAssetProcTmp initial value', () => {
        const formGroup = service.createRawAssetProcTmpFormGroup();

        const rawAssetProcTmp = service.getRawAssetProcTmp(formGroup) as any;

        expect(rawAssetProcTmp).toMatchObject({});
      });

      it('should return IRawAssetProcTmp', () => {
        const formGroup = service.createRawAssetProcTmpFormGroup(sampleWithRequiredData);

        const rawAssetProcTmp = service.getRawAssetProcTmp(formGroup) as any;

        expect(rawAssetProcTmp).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRawAssetProcTmp should not enable id FormControl', () => {
        const formGroup = service.createRawAssetProcTmpFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRawAssetProcTmp should disable id FormControl', () => {
        const formGroup = service.createRawAssetProcTmpFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
