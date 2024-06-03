import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 3407,
  listPrice: 11806.21,
  discontinued: false,
};

export const sampleWithPartialData: IProduct = {
  id: 2109,
  productSKU: 'boo',
  productName: 'since',
  description: '../fake-data/blob/hipster.txt',
  standardCost: 8333.77,
  listPrice: 24584.37,
  quantityPerUnit: 'impede virtuous amortise',
  discontinued: false,
  minimumReorderQuantity: 9511,
  supplierIds: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IProduct = {
  id: 25653,
  productSKU: 'aha',
  productName: 'unhook and',
  description: '../fake-data/blob/hipster.txt',
  standardCost: 17551.08,
  listPrice: 461.13,
  reorderLevel: 12473,
  targetLevel: 15653,
  quantityPerUnit: 'intelligent relinquish however',
  discontinued: true,
  minimumReorderQuantity: 24829,
  suggestedCategory: 'lending failing',
  attachments: '../fake-data/blob/hipster.png',
  attachmentsContentType: 'unknown',
  supplierIds: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewProduct = {
  listPrice: 31642.28,
  discontinued: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
