import { IAssetCollection, NewAssetCollection } from './asset-collection.model';

export const sampleWithRequiredData: IAssetCollection = {
  id: 28965,
  name: 'frequent',
  activationStatus: 'ON_HOLD',
};

export const sampleWithPartialData: IAssetCollection = {
  id: 26704,
  name: 'swivel mostly',
  activationStatus: 'INVALID',
};

export const sampleWithFullData: IAssetCollection = {
  id: 9462,
  name: 'anenst shout lightscreen',
  fullFilenamePath: 'super',
  activationStatus: 'INVALID',
};

export const sampleWithNewData: NewAssetCollection = {
  name: 'concerned enlarge factorise',
  activationStatus: 'PENDENT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
