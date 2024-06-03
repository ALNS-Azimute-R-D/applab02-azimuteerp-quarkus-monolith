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
  technicalEnvironmentsDetails: '../fake-data/blob/hipster.txt',
  organizationStatus: 'WORKING',
  activationStatus: 'BLOCKED',
  logoImg: '../fake-data/blob/hipster.png',
  logoImgContentType: 'unknown',
};

export const sampleWithFullData: IOrganization = {
  id: 26222,
  acronym: 'dictionary yippee',
  businessCode: 'story-telling a',
  hierarchicalLevel: 'on',
  name: 'noisily',
  description: 'plastic',
  businessHandlerClazz: 'signature',
  mainContactPersonDetails: '../fake-data/blob/hipster.txt',
  technicalEnvironmentsDetails: '../fake-data/blob/hipster.txt',
  commonCustomAttributesDetails: '../fake-data/blob/hipster.txt',
  organizationStatus: 'PENDENT',
  activationStatus: 'INVALID',
  logoImg: '../fake-data/blob/hipster.png',
  logoImgContentType: 'unknown',
};

export const sampleWithNewData: NewOrganization = {
  acronym: 'angry',
  businessCode: 'below calmly so',
  name: 'anti for duh',
  description: 'terraform brr speedily',
  organizationStatus: 'PENDENT',
  activationStatus: 'INVALID',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
