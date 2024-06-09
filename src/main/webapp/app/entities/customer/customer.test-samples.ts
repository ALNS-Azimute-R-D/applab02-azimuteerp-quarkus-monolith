import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 5009,
  customerBusinessCode: 'fixed joyous hu',
  fullname: 'apud bah',
  customerStatus: 'IN_FAILURE',
  activationStatus: 'INACTIVE',
};

export const sampleWithPartialData: ICustomer = {
  id: 20700,
  customerBusinessCode: 'apt',
  fullname: 'before',
  customAttributesDetailsJSON: 'aside times tinker',
  customerStatus: 'PENDENT',
  activationStatus: 'ON_HOLD',
};

export const sampleWithFullData: ICustomer = {
  id: 16227,
  customerBusinessCode: 'governance glea',
  fullname: 'ouch for wary',
  customAttributesDetailsJSON: 'down',
  customerStatus: 'UNDER_EVALUATION',
  activationStatus: 'ACTIVE',
};

export const sampleWithNewData: NewCustomer = {
  customerBusinessCode: 'bag failing meh',
  fullname: 'dutiful so elastic',
  customerStatus: 'UNDER_EVALUATION',
  activationStatus: 'INACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
