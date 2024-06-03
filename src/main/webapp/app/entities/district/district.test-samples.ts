import { IDistrict, NewDistrict } from './district.model';

export const sampleWithRequiredData: IDistrict = {
  id: 20215,
  acronym: 'survey',
  name: 'whoa',
};

export const sampleWithPartialData: IDistrict = {
  id: 26649,
  acronym: 'where br',
  name: 'free patiently except',
};

export const sampleWithFullData: IDistrict = {
  id: 29364,
  acronym: 'silly pe',
  name: 'comparison',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithNewData: NewDistrict = {
  acronym: 'fooey un',
  name: 'tensely kindly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
