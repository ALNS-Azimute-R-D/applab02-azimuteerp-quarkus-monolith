import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 26290,
  businessCode: 'now provided',
  customerUserId: 'establishment cathedral rich',
  placedDate: dayjs('2024-06-03T14:15'),
  status: 'CANCELLED',
};

export const sampleWithPartialData: IOrder = {
  id: 2793,
  businessCode: 'drat',
  customerUserId: 'brr helplessly upon',
  placedDate: dayjs('2024-06-03T21:40'),
  totalTaxValue: 31638.47,
  totalDueValue: 25275.1,
  status: 'CANCELLED',
};

export const sampleWithFullData: IOrder = {
  id: 29175,
  businessCode: 'yahoo background res',
  customerUserId: 'chomp',
  placedDate: dayjs('2024-06-03T19:14'),
  totalTaxValue: 5736.25,
  totalDueValue: 30931.09,
  status: 'CANCELLED',
  invoiceId: 3522,
  estimatedDeliveryDate: dayjs('2024-06-03T13:17'),
};

export const sampleWithNewData: NewOrder = {
  businessCode: 'circa',
  customerUserId: 'antique concerning within',
  placedDate: dayjs('2024-06-03T04:34'),
  status: 'COMPLETED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
