import { IOrganizationAttribute, NewOrganizationAttribute } from './organization-attribute.model';

export const sampleWithRequiredData: IOrganizationAttribute = {
  id: 18565,
};

export const sampleWithPartialData: IOrganizationAttribute = {
  id: 25359,
  attributeName: 'commerce rush',
};

export const sampleWithFullData: IOrganizationAttribute = {
  id: 9352,
  attributeName: 'moral bashfully',
  attributeValue: 'gosh wilt',
};

export const sampleWithNewData: NewOrganizationAttribute = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
