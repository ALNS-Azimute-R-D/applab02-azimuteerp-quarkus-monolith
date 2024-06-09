import { ITypeOfOrganization, NewTypeOfOrganization } from './type-of-organization.model';

export const sampleWithRequiredData: ITypeOfOrganization = {
  id: 21906,
  acronym: 'deceivingly',
  name: 'decode bold',
  description: 'oxford',
};

export const sampleWithPartialData: ITypeOfOrganization = {
  id: 12436,
  acronym: 'fooey',
  name: 'extend yum fox',
  description: 'crop',
  businessHandlerClazz: 'irritably',
};

export const sampleWithFullData: ITypeOfOrganization = {
  id: 127,
  acronym: 'though meanwhile',
  name: 'illiterate',
  description: 'whoa if',
  businessHandlerClazz: 'fooey because afore',
};

export const sampleWithNewData: NewTypeOfOrganization = {
  acronym: 'cheek josh following',
  name: 'seep',
  description: 'phew boo quizzically',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
