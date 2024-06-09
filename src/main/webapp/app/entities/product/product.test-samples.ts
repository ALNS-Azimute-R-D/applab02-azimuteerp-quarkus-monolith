import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 12949,
  listPrice: 11769.85,
  discontinued: true,
  activationStatus: 'INACTIVE',
};

export const sampleWithPartialData: IProduct = {
  id: 26883,
  productSKU: 'eventually bran whether',
  description: 'recommence passionate',
  standardCost: 12702.34,
  listPrice: 16341.34,
  reorderLevel: 4669,
  targetLevel: 20452,
  discontinued: true,
  suggestedCategory: 'chainstay',
  attachments: '../fake-data/blob/hipster.png',
  attachmentsContentType: 'unknown',
  activationStatus: 'ACTIVE',
};

export const sampleWithFullData: IProduct = {
  id: 26859,
  productSKU: 'silently existence',
  productName: 'paperback entity whether',
  description: 'root mislead corps',
  standardCost: 14657.44,
  listPrice: 31642.28,
  reorderLevel: 14898,
  targetLevel: 26123,
  quantityPerUnit: 'er',
  discontinued: true,
  minimumReorderQuantity: 18574,
  suggestedCategory: 'consequently generally',
  attachments: '../fake-data/blob/hipster.png',
  attachmentsContentType: 'unknown',
  activationStatus: 'ACTIVE',
};

export const sampleWithNewData: NewProduct = {
  listPrice: 25270.85,
  discontinued: true,
  activationStatus: 'INVALID',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
