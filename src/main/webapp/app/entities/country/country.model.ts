import { ContinentEnum } from 'app/entities/enumerations/continent-enum.model';

export interface ICountry {
  id: number;
  acronym?: string | null;
  name?: string | null;
  continent?: keyof typeof ContinentEnum | null;
  geoPolygonArea?: string | null;
  geoPolygonAreaContentType?: string | null;
}

export type NewCountry = Omit<ICountry, 'id'> & { id: null };
