import { IPaymentMethod, NewPaymentMethod } from './payment-method.model';

export const sampleWithRequiredData: IPaymentMethod = {
  id: 15658,
  code: 'vermicelli',
  description: 'bah untimely mmm',
};

export const sampleWithPartialData: IPaymentMethod = {
  id: 31626,
  code: 'fit',
  description: 'bump searchingly',
};

export const sampleWithFullData: IPaymentMethod = {
  id: 21077,
  code: 'fooey',
  description: 'flawless vessel',
  businessHandlerClazz: 'wrongly whose inasmuch',
};

export const sampleWithNewData: NewPaymentMethod = {
  code: 'nationalis',
  description: 'butcher over',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
