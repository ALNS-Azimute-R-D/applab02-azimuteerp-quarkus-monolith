import { IBrand, NewBrand } from './brand.model';

export const sampleWithRequiredData: IBrand = {
  id: 32090,
  acronym: 'fearless since once',
  name: 'knowingly',
};

export const sampleWithPartialData: IBrand = {
  id: 30686,
  acronym: 'go finished',
  name: 'progenitor of',
  logoBrand: '../fake-data/blob/hipster.png',
  logoBrandContentType: 'unknown',
};

export const sampleWithFullData: IBrand = {
  id: 27906,
  acronym: 'once pure vague',
  name: 'outside inside abnormally',
  description: 'obscure',
  logoBrand: '../fake-data/blob/hipster.png',
  logoBrandContentType: 'unknown',
};

export const sampleWithNewData: NewBrand = {
  acronym: 'below regularise',
  name: 'inhibition unless though',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
