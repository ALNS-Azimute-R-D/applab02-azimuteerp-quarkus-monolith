import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 26290,
  businessCode: 'now provided',
  placedDate: dayjs('2024-06-07T15:46'),
  status: 'COMPLETED',
  activationStatus: 'INVALID',
};

export const sampleWithPartialData: IOrder = {
  id: 16216,
  businessCode: 'cathedral',
  placedDate: dayjs('2024-06-07T16:52'),
  totalTaxValue: 23293.98,
  status: 'COMPLETED',
  activationStatus: 'BLOCKED',
};

export const sampleWithFullData: IOrder = {
  id: 26566,
  businessCode: 'reverse whirlpool',
  placedDate: dayjs('2024-06-07T13:17'),
  totalTaxValue: 14290.85,
  totalDueValue: 32011.89,
  status: 'CANCELLED',
  estimatedDeliveryDate: dayjs('2024-06-07T11:33'),
  activationStatus: 'BLOCKED',
};

export const sampleWithNewData: NewOrder = {
  businessCode: 'eek',
  placedDate: dayjs('2024-06-07T09:10'),
  status: 'COMPLETED',
  activationStatus: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
