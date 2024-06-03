import { IOrganizationDomain, NewOrganizationDomain } from './organization-domain.model';

export const sampleWithRequiredData: IOrganizationDomain = {
  id: 1992,
  domainAcronym: 'lively',
  name: 'pish',
  isVerified: true,
  activationStatus: 'INACTIVE',
};

export const sampleWithPartialData: IOrganizationDomain = {
  id: 11197,
  domainAcronym: 'easy shift',
  name: 'teletype',
  isVerified: false,
  businessHandlerClazz: 'nor gee',
  activationStatus: 'PENDENT',
};

export const sampleWithFullData: IOrganizationDomain = {
  id: 12326,
  domainAcronym: 'embezzle tilt',
  name: 'as',
  isVerified: false,
  businessHandlerClazz: 'witch-hunt consequently overshoot',
  activationStatus: 'ACTIVE',
};

export const sampleWithNewData: NewOrganizationDomain = {
  domainAcronym: 'discussion shopping',
  name: 'mid considerate abase',
  isVerified: false,
  activationStatus: 'INVALID',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
