import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 13801,
  businessCode: 'righteously pau',
  description: 'strew yahoo',
  numberOfInstallmentsOriginal: 7783,
  status: 'PAYING_IN_INSTALLMENTS',
};

export const sampleWithPartialData: IInvoice = {
  id: 17239,
  businessCode: 'ouch',
  invoiceDate: dayjs('2024-06-03T18:07'),
  description: 'miserably',
  taxValue: 13745.99,
  numberOfInstallmentsOriginal: 15531,
  numberOfInstallmentsPaid: 23555,
  status: 'CANCELLED',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IInvoice = {
  id: 27266,
  businessCode: 'worst fully who',
  originalOrderId: 17724,
  invoiceDate: dayjs('2024-06-03T15:24'),
  dueDate: dayjs('2024-06-03T21:31'),
  description: 'overlooked frantically whose',
  taxValue: 7951.74,
  shippingValue: 18037.51,
  amountDueValue: 20208.31,
  numberOfInstallmentsOriginal: 7155,
  numberOfInstallmentsPaid: 17634,
  amountPaidValue: 21552.67,
  status: 'CANCELLED',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewInvoice = {
  businessCode: 'opulent hypocho',
  description: 'interloper likewise psst',
  numberOfInstallmentsOriginal: 13408,
  status: 'PAYING_IN_INSTALLMENTS',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
