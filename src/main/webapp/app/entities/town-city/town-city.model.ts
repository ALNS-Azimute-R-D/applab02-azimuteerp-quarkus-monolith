import { IProvince } from 'app/entities/province/province.model';

export interface ITownCity {
  id: number;
  acronym?: string | null;
  name?: string | null;
  geoPolygonArea?: string | null;
  geoPolygonAreaContentType?: string | null;
  province?: Pick<IProvince, 'id' | 'name'> | null;
}

export type NewTownCity = Omit<ITownCity, 'id'> & { id: null };
