import { ICountry } from 'app/entities/country/country.model';

export interface IProvince {
  id: number;
  acronym?: string | null;
  name?: string | null;
  geoPolygonArea?: string | null;
  geoPolygonAreaContentType?: string | null;
  country?: Pick<ICountry, 'id' | 'name'> | null;
}

export type NewProvince = Omit<IProvince, 'id'> & { id: null };
