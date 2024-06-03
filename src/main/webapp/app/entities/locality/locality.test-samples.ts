import { ILocality, NewLocality } from './locality.model';

export const sampleWithRequiredData: ILocality = {
  id: 17588,
  acronym: 'chino',
  name: 'vain winding kissingly',
};

export const sampleWithPartialData: ILocality = {
  id: 155,
  acronym: 'ironclad',
  name: 'under',
};

export const sampleWithFullData: ILocality = {
  id: 3473,
  acronym: 'giant th',
  name: 'even within delayed',
  description: '../fake-data/blob/hipster.txt',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithNewData: NewLocality = {
  acronym: 'warmly u',
  name: 'whereas gracefully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
