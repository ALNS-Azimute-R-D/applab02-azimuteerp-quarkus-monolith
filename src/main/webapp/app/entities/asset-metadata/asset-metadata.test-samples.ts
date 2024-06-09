import { IAssetMetadata, NewAssetMetadata } from './asset-metadata.model';

export const sampleWithRequiredData: IAssetMetadata = {
  id: 31019,
};

export const sampleWithPartialData: IAssetMetadata = {
  id: 15603,
};

export const sampleWithFullData: IAssetMetadata = {
  id: 342,
  metadataDetailsJSON: 'apud zowie sans',
};

export const sampleWithNewData: NewAssetMetadata = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
