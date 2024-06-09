import { IAssetType, NewAssetType } from './asset-type.model';

export const sampleWithRequiredData: IAssetType = {
  id: 24626,
  name: 'thirst',
};

export const sampleWithPartialData: IAssetType = {
  id: 2757,
  name: 'lazily separate into',
  handlerClazzName: 'gosh recovery gadzooks',
  customAttributesDetailsJSON: 'insignificant imaginative',
};

export const sampleWithFullData: IAssetType = {
  id: 29576,
  acronym: 'whereas barring without',
  name: 'always',
  description: 'geez willing',
  handlerClazzName: 'forenenst daybed flourish',
  customAttributesDetailsJSON: 'finally',
};

export const sampleWithNewData: NewAssetType = {
  name: 'courteous',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
