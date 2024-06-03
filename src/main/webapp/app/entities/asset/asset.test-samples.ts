import { IAsset, NewAsset } from './asset.model';

export const sampleWithRequiredData: IAsset = {
  id: 29147,
  name: 'litigate ugly',
};

export const sampleWithPartialData: IAsset = {
  id: 2575,
  name: 'inwardly',
  storageTypeUsed: 'BLOB_IN_DB',
};

export const sampleWithFullData: IAsset = {
  id: 2084,
  uid: '634609f0-b2af-47b1-8496-c577312ea535',
  name: 'now cathedral always',
  storageTypeUsed: 'REMOTE_MINIO',
  fullFilenamePath: 'warm',
  status: 'DELETED',
  preferredPurpose: 'LOGO_IMG',
  assetContentAsBlob: '../fake-data/blob/hipster.png',
  assetContentAsBlobContentType: 'unknown',
};

export const sampleWithNewData: NewAsset = {
  name: 'inside sleepy',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
