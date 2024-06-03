import dayjs from 'dayjs/esm';

import { IStockLevel, NewStockLevel } from './stock-level.model';

export const sampleWithRequiredData: IStockLevel = {
  id: 21534,
  lastModifiedDate: dayjs('2024-06-03T03:21'),
  ramainingQuantity: 8787,
};

export const sampleWithPartialData: IStockLevel = {
  id: 3325,
  lastModifiedDate: dayjs('2024-06-03T07:50'),
  ramainingQuantity: 19693,
};

export const sampleWithFullData: IStockLevel = {
  id: 5437,
  lastModifiedDate: dayjs('2024-06-03T10:37'),
  ramainingQuantity: 26498,
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewStockLevel = {
  lastModifiedDate: dayjs('2024-06-03T07:03'),
  ramainingQuantity: 9432,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
