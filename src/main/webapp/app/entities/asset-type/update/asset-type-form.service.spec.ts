import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../asset-type.test-samples';

import { AssetTypeFormService } from './asset-type-form.service';

describe('AssetType Form Service', () => {
  let service: AssetTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssetTypeFormService);
  });

  describe('Service methods', () => {
    describe('createAssetTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssetTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            handlerClazzName: expect.any(Object),
            customAttributesDetailsJSON: expect.any(Object),
          }),
        );
      });

      it('passing IAssetType should create a new form with FormGroup', () => {
        const formGroup = service.createAssetTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            handlerClazzName: expect.any(Object),
            customAttributesDetailsJSON: expect.any(Object),
          }),
        );
      });
    });

    describe('getAssetType', () => {
      it('should return NewAssetType for default AssetType initial value', () => {
        const formGroup = service.createAssetTypeFormGroup(sampleWithNewData);

        const assetType = service.getAssetType(formGroup) as any;

        expect(assetType).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssetType for empty AssetType initial value', () => {
        const formGroup = service.createAssetTypeFormGroup();

        const assetType = service.getAssetType(formGroup) as any;

        expect(assetType).toMatchObject({});
      });

      it('should return IAssetType', () => {
        const formGroup = service.createAssetTypeFormGroup(sampleWithRequiredData);

        const assetType = service.getAssetType(formGroup) as any;

        expect(assetType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssetType should not enable id FormControl', () => {
        const formGroup = service.createAssetTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssetType should disable id FormControl', () => {
        const formGroup = service.createAssetTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
