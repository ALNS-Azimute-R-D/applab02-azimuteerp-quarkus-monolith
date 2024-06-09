import dayjs from 'dayjs/esm';

import { IInventoryTransaction, NewInventoryTransaction } from './inventory-transaction.model';

export const sampleWithRequiredData: IInventoryTransaction = {
  id: 9510,
  invoiceId: 7029,
  quantity: 3233,
  activationStatus: 'ACTIVE',
};

export const sampleWithPartialData: IInventoryTransaction = {
  id: 12814,
  invoiceId: 22021,
  transactionModifiedDate: dayjs('2024-06-07T12:01'),
  quantity: 19044,
  transactionComments: 'mid curry',
  activationStatus: 'PENDENT',
};

export const sampleWithFullData: IInventoryTransaction = {
  id: 19855,
  invoiceId: 3521,
  transactionCreatedDate: dayjs('2024-06-07T22:47'),
  transactionModifiedDate: dayjs('2024-06-07T21:23'),
  quantity: 29690,
  transactionComments: 'whose',
  activationStatus: 'INVALID',
};

export const sampleWithNewData: NewInventoryTransaction = {
  invoiceId: 6104,
  quantity: 3642,
  activationStatus: 'PENDENT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
