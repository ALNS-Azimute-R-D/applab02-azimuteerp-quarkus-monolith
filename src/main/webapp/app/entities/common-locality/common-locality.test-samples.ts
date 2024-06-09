import { ICommonLocality, NewCommonLocality } from './common-locality.model';

export const sampleWithRequiredData: ICommonLocality = {
  id: 16160,
  acronym: 'grandiose huzzah tut',
  name: 'zoot-suit',
  streetAddress: 'geez impish',
  postalCode: 'rightfull',
};

export const sampleWithPartialData: ICommonLocality = {
  id: 554,
  acronym: 'brr barring',
  name: 'interestingly',
  description: 'behind',
  streetAddress: 'regard',
  postalCode: 'save',
};

export const sampleWithFullData: ICommonLocality = {
  id: 31727,
  acronym: 'wherever',
  name: 'large agonizing',
  description: 'almond acceptable',
  streetAddress: 'indeed',
  houseNumber: 'once provided journe',
  locationName: 'since boohoo',
  postalCode: 'although ',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithNewData: NewCommonLocality = {
  acronym: 'wrathful',
  name: 'gadzooks playfully ugh',
  streetAddress: 'soupy when whoa',
  postalCode: 'obedientl',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
