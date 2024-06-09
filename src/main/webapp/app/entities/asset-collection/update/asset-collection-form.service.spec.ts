import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../asset-collection.test-samples';

import { AssetCollectionFormService } from './asset-collection-form.service';

describe('AssetCollection Form Service', () => {
  let service: AssetCollectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssetCollectionFormService);
  });

  describe('Service methods', () => {
    describe('createAssetCollectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAssetCollectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fullFilenamePath: expect.any(Object),
            activationStatus: expect.any(Object),
            assets: expect.any(Object),
          }),
        );
      });

      it('passing IAssetCollection should create a new form with FormGroup', () => {
        const formGroup = service.createAssetCollectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fullFilenamePath: expect.any(Object),
            activationStatus: expect.any(Object),
            assets: expect.any(Object),
          }),
        );
      });
    });

    describe('getAssetCollection', () => {
      it('should return NewAssetCollection for default AssetCollection initial value', () => {
        const formGroup = service.createAssetCollectionFormGroup(sampleWithNewData);

        const assetCollection = service.getAssetCollection(formGroup) as any;

        expect(assetCollection).toMatchObject(sampleWithNewData);
      });

      it('should return NewAssetCollection for empty AssetCollection initial value', () => {
        const formGroup = service.createAssetCollectionFormGroup();

        const assetCollection = service.getAssetCollection(formGroup) as any;

        expect(assetCollection).toMatchObject({});
      });

      it('should return IAssetCollection', () => {
        const formGroup = service.createAssetCollectionFormGroup(sampleWithRequiredData);

        const assetCollection = service.getAssetCollection(formGroup) as any;

        expect(assetCollection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAssetCollection should not enable id FormControl', () => {
        const formGroup = service.createAssetCollectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAssetCollection should disable id FormControl', () => {
        const formGroup = service.createAssetCollectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
