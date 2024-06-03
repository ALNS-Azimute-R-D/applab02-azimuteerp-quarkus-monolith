import { ITypeOfOrganization, NewTypeOfOrganization } from './type-of-organization.model';

export const sampleWithRequiredData: ITypeOfOrganization = {
  id: 21906,
  acronym: 'deceivingly',
  name: 'decode bold',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: ITypeOfOrganization = {
  id: 11644,
  acronym: 'instead coruscate',
  name: 'handmade',
  description: '../fake-data/blob/hipster.txt',
  businessHandlerClazz: 'yum',
};

export const sampleWithFullData: ITypeOfOrganization = {
  id: 7506,
  acronym: 'meanwhile',
  name: 'madly after finally',
  description: '../fake-data/blob/hipster.txt',
  businessHandlerClazz: 'meanwhile puzzling',
};

export const sampleWithNewData: NewTypeOfOrganization = {
  acronym: 'barrack whoever',
  name: 'provided',
  description: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
