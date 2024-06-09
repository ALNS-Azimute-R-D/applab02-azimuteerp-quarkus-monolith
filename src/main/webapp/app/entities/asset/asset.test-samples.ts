import { IAsset, NewAsset } from './asset.model';

export const sampleWithRequiredData: IAsset = {
  id: 29147,
  name: 'litigate ugly',
  activationStatus: 'BLOCKED',
};

export const sampleWithPartialData: IAsset = {
  id: 2575,
  name: 'inwardly',
  storageTypeUsed: 'BLOB_IN_DB',
  activationStatus: 'INACTIVE',
};

export const sampleWithFullData: IAsset = {
  id: 12817,
  name: 'reshape',
  storageTypeUsed: 'BLOB_IN_DB',
  fullFilenamePath: 'excluding yowza unto',
  status: 'ENABLED',
  preferredPurpose: 'ANY_OFFICE_FILE_TYPE',
  assetContentAsBlob: '../fake-data/blob/hipster.png',
  assetContentAsBlobContentType: 'unknown',
  activationStatus: 'ON_HOLD',
};

export const sampleWithNewData: NewAsset = {
  name: 'separately',
  activationStatus: 'INVALID',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
