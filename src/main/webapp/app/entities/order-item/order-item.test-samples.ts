import { IOrderItem, NewOrderItem } from './order-item.model';

export const sampleWithRequiredData: IOrderItem = {
  id: 1622,
  quantity: 18255,
  totalPrice: 18622.09,
  status: 'AVAILABLE',
};

export const sampleWithPartialData: IOrderItem = {
  id: 10224,
  quantity: 32017,
  totalPrice: 3914.98,
  status: 'OUT_OF_STOCK',
};

export const sampleWithFullData: IOrderItem = {
  id: 11101,
  quantity: 21982,
  totalPrice: 548.01,
  status: 'AVAILABLE',
};

export const sampleWithNewData: NewOrderItem = {
  quantity: 24222,
  totalPrice: 24293.65,
  status: 'OUT_OF_STOCK',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
