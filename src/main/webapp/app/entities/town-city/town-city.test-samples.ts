import { ITownCity, NewTownCity } from './town-city.model';

export const sampleWithRequiredData: ITownCity = {
  id: 16922,
  acronym: 'given bo',
  name: 'within with',
};

export const sampleWithPartialData: ITownCity = {
  id: 12317,
  acronym: 'when dec',
  name: 'generally',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithFullData: ITownCity = {
  id: 22627,
  acronym: 'um sizzl',
  name: 'whoa although',
  geoPolygonArea: '../fake-data/blob/hipster.png',
  geoPolygonAreaContentType: 'unknown',
};

export const sampleWithNewData: NewTownCity = {
  acronym: 'hence ah',
  name: 'shed notwithstanding diner',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
