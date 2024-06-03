import { ITownCity } from 'app/entities/town-city/town-city.model';

export interface IDistrict {
  id: number;
  acronym?: string | null;
  name?: string | null;
  geoPolygonArea?: string | null;
  geoPolygonAreaContentType?: string | null;
  townCity?: Pick<ITownCity, 'id' | 'name'> | null;
}

export type NewDistrict = Omit<IDistrict, 'id'> & { id: null };
