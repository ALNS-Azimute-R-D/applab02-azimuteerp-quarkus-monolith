import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 29144,
  acronym: 'mou',
  name: 'um briskly constrict',
  continent: 'SOUTH_AMERICA',
};

export const sampleWithPartialData: ICountry = {
  id: 16668,
  acronym: 'pro',
  name: 'nearly concerning',
  continent: 'NORTH_AMERICA',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithFullData: ICountry = {
  id: 31487,
  acronym: 'how',
  name: 'decentralise',
  continent: 'EUROPE',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithNewData: NewCountry = {
  acronym: 'tru',
  name: 'lest afore',
  continent: 'ASIA',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
