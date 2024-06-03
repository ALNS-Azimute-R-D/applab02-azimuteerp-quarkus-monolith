import { IArticle, NewArticle } from './article.model';

export const sampleWithRequiredData: IArticle = {
  id: 7398,
  inventoryProductId: 13320,
  itemSize: 'XXL',
};

export const sampleWithPartialData: IArticle = {
  id: 5521,
  inventoryProductId: 19899,
  itemSize: 'L',
  assetsCollectionUUID: 'boohoo knowledgeable',
  isEnabled: false,
};

export const sampleWithFullData: IArticle = {
  id: 6126,
  inventoryProductId: 2194,
  customName: 'yum almost scandalize',
  customDescription: '../fake-data/blob/hipster.txt',
  priceValue: 11908.29,
  itemSize: 'XL',
  assetsCollectionUUID: 'excluding damp',
  isEnabled: true,
};

export const sampleWithNewData: NewArticle = {
  inventoryProductId: 25102,
  itemSize: 'L',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
