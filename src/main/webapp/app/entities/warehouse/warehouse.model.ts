export interface IWarehouse {
  id: number;
  acronym?: string | null;
  name?: string | null;
  description?: string | null;
  streetAddress?: string | null;
  houseNumber?: string | null;
  locationName?: string | null;
  postalCode?: string | null;
  pointLocation?: string | null;
  pointLocationContentType?: string | null;
  mainEmail?: string | null;
  landPhoneNumber?: string | null;
  mobilePhoneNumber?: string | null;
  faxNumber?: string | null;
  extraDetails?: string | null;
}

export type NewWarehouse = Omit<IWarehouse, 'id'> & { id: null };
