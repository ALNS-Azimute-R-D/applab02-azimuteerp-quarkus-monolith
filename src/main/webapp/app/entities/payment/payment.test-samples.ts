import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 10755,
  installmentNumber: 31906,
  paymentDueDate: dayjs('2024-06-07T09:36'),
  paymentPaidDate: dayjs('2024-06-07T16:53'),
  paymentAmount: 11259.51,
  typeOfPayment: 'OTHER',
  statusPayment: 'PAID',
  activationStatus: 'BLOCKED',
};

export const sampleWithPartialData: IPayment = {
  id: 406,
  installmentNumber: 3302,
  paymentDueDate: dayjs('2024-06-07T10:46'),
  paymentPaidDate: dayjs('2024-06-07T16:14'),
  paymentAmount: 4944.33,
  typeOfPayment: 'OTHER',
  statusPayment: 'OPEN',
  customAttributesDetailsJSON: 'laborer save understudy',
  activationStatus: 'BLOCKED',
};

export const sampleWithFullData: IPayment = {
  id: 9694,
  installmentNumber: 30262,
  paymentDueDate: dayjs('2024-06-07T22:47'),
  paymentPaidDate: dayjs('2024-06-07T21:40'),
  paymentAmount: 20263.27,
  typeOfPayment: 'OTHER',
  statusPayment: 'PAID',
  customAttributesDetailsJSON: 'because aromatic',
  activationStatus: 'BLOCKED',
};

export const sampleWithNewData: NewPayment = {
  installmentNumber: 21305,
  paymentDueDate: dayjs('2024-06-07T12:59'),
  paymentPaidDate: dayjs('2024-06-07T23:42'),
  paymentAmount: 25157.57,
  typeOfPayment: 'CREDIT',
  statusPayment: 'CANCELLED',
  activationStatus: 'INACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
