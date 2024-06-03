import { ICustomerType, NewCustomerType } from './customer-type.model';

export const sampleWithRequiredData: ICustomerType = {
  id: 6076,
  name: 'ouch defiantly',
};

export const sampleWithPartialData: ICustomerType = {
  id: 31886,
  name: 'freely pannier',
  description: 'unaccountably',
};

export const sampleWithFullData: ICustomerType = {
  id: 6954,
  name: 'likewise quizzical',
  description: 'phew along patty',
  businessHandlerClazz: 'back zowie',
};

export const sampleWithNewData: NewCustomerType = {
  name: 'spec',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
