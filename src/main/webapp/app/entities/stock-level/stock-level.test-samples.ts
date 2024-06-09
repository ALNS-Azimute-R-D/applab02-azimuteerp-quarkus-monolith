import dayjs from 'dayjs/esm';

import { IStockLevel, NewStockLevel } from './stock-level.model';

export const sampleWithRequiredData: IStockLevel = {
  id: 21534,
  lastModifiedDate: dayjs('2024-06-07T12:53'),
  remainingQuantity: 8787,
};

export const sampleWithPartialData: IStockLevel = {
  id: 3325,
  lastModifiedDate: dayjs('2024-06-07T17:22'),
  remainingQuantity: 19693,
};

export const sampleWithFullData: IStockLevel = {
  id: 5437,
  lastModifiedDate: dayjs('2024-06-07T20:10'),
  remainingQuantity: 26498,
  commonAttributesDetailsJSON: 'oppress push',
};

export const sampleWithNewData: NewStockLevel = {
  lastModifiedDate: dayjs('2024-06-07T09:10'),
  remainingQuantity: 25454,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
