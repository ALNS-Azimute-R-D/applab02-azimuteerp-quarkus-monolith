import { ICountry } from 'app/entities/country/country.model';

export interface ILocality {
  id: number;
  acronym?: string | null;
  name?: string | null;
  description?: string | null;
  geoPolygonArea?: string | null;
  geoPolygonAreaContentType?: string | null;
  country?: Pick<ICountry, 'id' | 'name'> | null;
}

export type NewLocality = Omit<ILocality, 'id'> & { id: null };
