import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../asset-metadata.test-samples';

import { AssetMetadataFormService } from './asset-metadata-form.service';

describe('AssetMetadata Form Service', () => {
  let service: AssetMetadataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssetMetadataFormService);
  });

  describe('Service methods', () => {
    describe('createAssetMetadataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssetMetadataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            metadataDetails: expect.any(Object),
            asset: expect.any(Object),
          }),
        );
      });

      it('passing IAssetMetadata should create a new form with FormGroup', () => {
        const formGroup = service.createAssetMetadataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            metadataDetails: expect.any(Object),
            asset: expect.any(Object),
          }),
        );
      });
    });

    describe('getAssetMetadata', () => {
      it('should return NewAssetMetadata for default AssetMetadata initial value', () => {
        const formGroup = service.createAssetMetadataFormGroup(sampleWithNewData);

        const assetMetadata = service.getAssetMetadata(formGroup) as any;

        expect(assetMetadata).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssetMetadata for empty AssetMetadata initial value', () => {
        const formGroup = service.createAssetMetadataFormGroup();

        const assetMetadata = service.getAssetMetadata(formGroup) as any;

        expect(assetMetadata).toMatchObject({});
      });

      it('should return IAssetMetadata', () => {
        const formGroup = service.createAssetMetadataFormGroup(sampleWithRequiredData);

        const assetMetadata = service.getAssetMetadata(formGroup) as any;

        expect(assetMetadata).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssetMetadata should not enable id FormControl', () => {
        const formGroup = service.createAssetMetadataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssetMetadata should disable id FormControl', () => {
        const formGroup = service.createAssetMetadataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
