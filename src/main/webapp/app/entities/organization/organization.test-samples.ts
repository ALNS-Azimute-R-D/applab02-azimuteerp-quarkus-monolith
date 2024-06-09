import { IOrganization, NewOrganization } from './organization.model';

export const sampleWithRequiredData: IOrganization = {
  id: 30849,
  acronym: 'before',
  businessCode: 'considering rec',
  name: 'between title forenenst',
  description: 'yippee excerpt medium',
  organizationStatus: 'READY_TO_START',
  activationStatus: 'PENDENT',
};

export const sampleWithPartialData: IOrganization = {
  id: 657,
  acronym: 'beyond scarce undern',
  businessCode: 'since propagate',
  name: 'pointless',
  description: 'anglicize',
  technicalEnvironmentsDetailsJSON: 'gosh ecumenist',
  organizationStatus: 'ONBOARDING',
  activationStatus: 'ACTIVE',
  logoImg: '../fake-data/blob/hipster.png',
  logoImgContentType: 'unknown',
};

export const sampleWithFullData: IOrganization = {
  id: 18950,
  acronym: 'story-telling awkwar',
  businessCode: 'on',
  hierarchicalLevel: 'noisily',
  name: 'plastic',
  description: 'signature',
  businessHandlerClazz: 'what vote till',
  mainContactPersonDetailsJSON: 'toothbrush',
  technicalEnvironmentsDetailsJSON: 'pish indeed',
  customAttributesDetailsJSON: 'skirt',
  organizationStatus: 'PENDENT',
  activationStatus: 'INVALID',
  logoImg: '../fake-data/blob/hipster.png',
  logoImgContentType: 'unknown',
};

export const sampleWithNewData: NewOrganization = {
  acronym: 'amusing',
  businessCode: 'pish',
  name: 'an for',
  description: 'funny fair enthral',
  organizationStatus: 'PENDENT',
  activationStatus: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
