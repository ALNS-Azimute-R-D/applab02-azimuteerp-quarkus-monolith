import { IDistrict } from 'app/entities/district/district.model';

export interface ICommonLocality {
  id: number;
  acronym?: string | null;
  name?: string | null;
  description?: string | null;
  streetAddress?: string | null;
  houseNumber?: string | null;
  locationName?: string | null;
  postalCode?: string | null;
  geoPolygonArea?: string | null;
  geoPolygonAreaContentType?: string | null;
  district?: Pick<IDistrict, 'id' | 'name'> | null;
}

export type NewCommonLocality = Omit<ICommonLocality, 'id'> & { id: null };
