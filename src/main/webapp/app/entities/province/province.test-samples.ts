import { IProvince, NewProvince } from './province.model';

export const sampleWithRequiredData: IProvince = {
  id: 25452,
  acronym: 'pal',
  name: 'voluminous near',
};

export const sampleWithPartialData: IProvince = {
  id: 28943,
  acronym: 'utt',
  name: 'hence',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithFullData: IProvince = {
  id: 14597,
  acronym: 'yip',
  name: 'fooey opera next',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithNewData: NewProvince = {
  acronym: 'bel',
  name: 'yahoo hmph subedit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
