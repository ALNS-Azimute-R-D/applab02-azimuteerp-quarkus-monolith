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
  billingDetailsJSON: 'whoever as',
  technicalEnvironmentsDetailsJSON: 'ha to',
  customAttributesDetailsJSON: 'near untimely',
  activationStatus: 'PENDENT',
};

export const sampleWithFullData: ITenant = {
  id: 21399,
  acronym: 'less as naughty',
  name: 'than who woot',
  description: 'soprano hence finally',
  customerBusinessCode: 'bite-sized',
  businessHandlerClazz: 'against dimple',
  mainContactPersonDetailsJSON: 'treasure hm',
  billingDetailsJSON: 'rag huge',
  technicalEnvironmentsDetailsJSON: 'fooey sidecar envious',
  customAttributesDetailsJSON: 'karate yahoo misguided',
  logoImg: '../fake-data/blob/hipster.png',
  logoImgContentType: 'unknown',
  activationStatus: 'BLOCKED',
};

export const sampleWithNewData: NewTenant = {
  acronym: 'below versus excited',
  name: 'finally why',
  description: 'disclosure ick',
  customerBusinessCode: 'forenenst up',
  activationStatus: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
