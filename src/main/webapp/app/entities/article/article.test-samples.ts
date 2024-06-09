import { IArticle, NewArticle } from './article.model';

export const sampleWithRequiredData: IArticle = {
  id: 7398,
  inventoryProductId: 13320,
  itemSize: 'XXL',
  activationStatus: 'BLOCKED',
};

export const sampleWithPartialData: IArticle = {
  id: 5521,
  inventoryProductId: 19899,
  customDescription: 'mankind prudent',
  priceValue: 15414.92,
  itemSize: 'XXL',
  activationStatus: 'ACTIVE',
};

export const sampleWithFullData: IArticle = {
  id: 2194,
  inventoryProductId: 25203,
  skuCode: 'naturalize quizzically',
  customName: 'officially zowie',
  customDescription: 'damp tomorrow',
  priceValue: 4617.82,
  itemSize: 'S',
  activationStatus: 'PENDENT',
};

export const sampleWithNewData: NewArticle = {
  inventoryProductId: 3499,
  itemSize: 'XL',
  activationStatus: 'INVALID',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
