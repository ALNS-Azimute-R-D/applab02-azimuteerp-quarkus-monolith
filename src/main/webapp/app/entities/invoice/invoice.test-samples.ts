import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 13801,
  businessCode: 'righteously pau',
  description: 'strew yahoo',
  numberOfInstallmentsOriginal: 7783,
  status: 'PAYING_IN_INSTALLMENTS',
  activationStatus: 'BLOCKED',
};

export const sampleWithPartialData: IInvoice = {
  id: 17239,
  businessCode: 'ouch',
  invoiceDate: dayjs('2024-06-08T03:39'),
  description: 'miserably',
  taxValue: 13745.99,
  numberOfInstallmentsOriginal: 15531,
  numberOfInstallmentsPaid: 23555,
  status: 'CANCELLED',
  customAttributesDetailsJSON: 'who yahoo flash',
  activationStatus: 'INVALID',
};

export const sampleWithFullData: IInvoice = {
  id: 17724,
  businessCode: 'galvanize',
  invoiceDate: dayjs('2024-06-07T16:23'),
  dueDate: dayjs('2024-06-07T17:34'),
  description: 'not pfft icy',
  taxValue: 10875.58,
  shippingValue: 12282.16,
  amountDueValue: 24573.1,
  numberOfInstallmentsOriginal: 11155,
  numberOfInstallmentsPaid: 26332,
  amountPaidValue: 27134.69,
  status: 'ISSUED',
  customAttributesDetailsJSON: 'quest interloper likewise',
  activationStatus: 'ACTIVE',
};

export const sampleWithNewData: NewInvoice = {
  businessCode: 'likewise demora',
  description: 'quizzically reproachfully ugh',
  numberOfInstallmentsOriginal: 262,
  status: 'PAID_ONCE',
  activationStatus: 'INACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
