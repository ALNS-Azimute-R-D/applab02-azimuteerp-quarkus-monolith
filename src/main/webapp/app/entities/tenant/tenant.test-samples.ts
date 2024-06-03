import { ITenant, NewTenant } from './tenant.model';

export const sampleWithRequiredData: ITenant = {
  id: 26814,
  acronym: 'coarse bravely',
  name: 'fussy seldom astride',
  description: 'freeload',
  customerBusinessCode: 'usefully um',
  activationStatus: 'PENDENT',
};

export const sampleWithPartialData: ITenant = {
  id: 27736,
  acronym: 'provided',
  name: 'whether despite athlete',
  description: 'fooey',
  customerBusinessCode: 'towards whoa',
  billingDetails: '../fake-data/blob/hipster.txt',
  technicalEnvironmentsDetails: '../fake-data/blob/hipster.txt',
  commonCustomAttributesDetails: '../fake-data/blob/hipster.txt',
  activationStatus: 'PENDENT',
};

export const sampleWithFullData: ITenant = {
  id: 22744,
  acronym: 'terrific watercress ',
  name: 'more triple major',
  description: 'exemplary concerning',
  customerBusinessCode: 'naughty',
  businessHandlerClazz: 'than who woot',
  mainContactPersonDetails: '../fake-data/blob/hipster.txt',
  billingDetails: '../fake-data/blob/hipster.txt',
  technicalEnvironmentsDetails: '../fake-data/blob/hipster.txt',
  commonCustomAttributesDetails: '../fake-data/blob/hipster.txt',
  logoImg: '../fake-data/blob/hipster.png',
  logoImgContentType: 'unknown',
  activationStatus: 'INVALID',
};

export const sampleWithNewData: NewTenant = {
  acronym: 'nonstop',
  name: 'yowza skull plane',
  description: 'brr drat',
  customerBusinessCode: 'rubbish gadzook',
  activationStatus: 'ON_HOLD',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
