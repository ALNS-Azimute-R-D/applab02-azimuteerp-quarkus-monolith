import { IAssetType, NewAssetType } from './asset-type.model';

export const sampleWithRequiredData: IAssetType = {
  id: 24626,
  name: 'thirst',
};

export const sampleWithPartialData: IAssetType = {
  id: 2757,
  name: 'lazily separate into',
  handlerClazzName: 'gosh recovery gadzooks',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IAssetType = {
  id: 13767,
  acronym: 'gah thief',
  name: 'whereas barring without',
  description: 'always',
  handlerClazzName: 'geez willing',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewAssetType = {
  name: 'forenenst daybed flourish',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
