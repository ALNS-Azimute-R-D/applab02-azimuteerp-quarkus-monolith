import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 5009,
  customerBusinessCode: 'fixed joyous hu',
  name: 'apud bah',
  email: '.3\\;1j@P_k$8~.rt1\\',
  status: 'UNDER_EVALUATION',
  active: false,
};

export const sampleWithPartialData: ICustomer = {
  id: 6595,
  customerBusinessCode: 'overuse boo for',
  name: 'giant fabulous',
  description: '../fake-data/blob/hipster.txt',
  email: 'f@eg.(pE>',
  status: 'PENDENT',
  active: true,
};

export const sampleWithFullData: ICustomer = {
  id: 18066,
  customerBusinessCode: 'knead cautiousl',
  name: 'ick distribute quarterly',
  description: '../fake-data/blob/hipster.txt',
  email: '5TQ-@".Wc`DN',
  addressDetails: 'after fiercely hence',
  zipCode: '48653',
  keycloakGroupDetails: 'yuck eek',
  status: 'UNDER_EVALUATION',
  active: true,
};

export const sampleWithNewData: NewCustomer = {
  customerBusinessCode: 'concerning',
  name: 'incomparable exterminate aside',
  email: 'F|@yIP.p',
  status: 'ONBOARDING',
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
