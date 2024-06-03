import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 10755,
  installmentNumber: 31906,
  paymentDueDate: dayjs('2024-06-03T00:04'),
  paymentPaidDate: dayjs('2024-06-03T07:21'),
  paymentAmount: 11259.51,
  typeOfPayment: 'OTHER',
  status: 'PAID',
};

export const sampleWithPartialData: IPayment = {
  id: 13961,
  installmentNumber: 406,
  paymentDueDate: dayjs('2024-06-03T20:10'),
  paymentPaidDate: dayjs('2024-06-03T01:13'),
  paymentAmount: 21708.99,
  typeOfPayment: 'CASH',
  status: 'CANCELLED',
};

export const sampleWithFullData: IPayment = {
  id: 6159,
  installmentNumber: 28851,
  paymentDueDate: dayjs('2024-06-03T12:59'),
  paymentPaidDate: dayjs('2024-06-03T20:13'),
  paymentAmount: 17857.18,
  typeOfPayment: 'OTHER',
  status: 'PAID',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPayment = {
  installmentNumber: 4239,
  paymentDueDate: dayjs('2024-06-03T10:44'),
  paymentPaidDate: dayjs('2024-06-03T09:08'),
  paymentAmount: 18681.01,
  typeOfPayment: 'CASH',
  status: 'DELAYED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
