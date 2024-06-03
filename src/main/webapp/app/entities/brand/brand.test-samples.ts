import { IBrand, NewBrand } from './brand.model';

export const sampleWithRequiredData: IBrand = {
  id: 32090,
  acronym: 'fearless since once',
  name: 'knowingly',
};

export const sampleWithPartialData: IBrand = {
  id: 8867,
  acronym: 'like manor across',
  name: 'bronze downright',
};

export const sampleWithFullData: IBrand = {
  id: 19285,
  acronym: 'smart',
  name: 'vague squirrel',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewBrand = {
  acronym: 'inside abnormally st',
  name: 'phooey snivel',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
