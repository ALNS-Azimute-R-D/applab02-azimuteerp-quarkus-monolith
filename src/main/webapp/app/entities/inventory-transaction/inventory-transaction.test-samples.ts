import dayjs from 'dayjs/esm';

import { IInventoryTransaction, NewInventoryTransaction } from './inventory-transaction.model';

export const sampleWithRequiredData: IInventoryTransaction = {
  id: 9510,
  invoiceId: 7029,
  quantity: 3233,
};

export const sampleWithPartialData: IInventoryTransaction = {
  id: 6008,
  invoiceId: 12814,
  transactionCreatedDate: dayjs('2024-06-03T06:33'),
  quantity: 27564,
  comments: 'optimistically regarding',
};

export const sampleWithFullData: IInventoryTransaction = {
  id: 7566,
  invoiceId: 21558,
  transactionCreatedDate: dayjs('2024-06-03T08:08'),
  transactionModifiedDate: dayjs('2024-06-03T20:06'),
  quantity: 12876,
  comments: 'how furthermore',
};

export const sampleWithNewData: NewInventoryTransaction = {
  invoiceId: 27530,
  quantity: 2279,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
