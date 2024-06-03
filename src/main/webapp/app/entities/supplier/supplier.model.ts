import { IProduct } from 'app/entities/product/product.model';

export interface ISupplier {
  id: number;
  acronym?: string | null;
  companyName?: string | null;
  representativeLastName?: string | null;
  representativeFirstName?: string | null;
  jobTitle?: string | null;
  streetAddress?: string | null;
  houseNumber?: string | null;
  locationName?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  zipPostalCode?: string | null;
  countryRegion?: string | null;
  webPage?: string | null;
  pointLocation?: string | null;
  pointLocationContentType?: string | null;
  mainEmail?: string | null;
  landPhoneNumber?: string | null;
  mobilePhoneNumber?: string | null;
  faxNumber?: string | null;
  extraDetails?: string | null;
  productsLists?: Pick<IProduct, 'id'>[] | null;
}

export type NewSupplier = Omit<ISupplier, 'id'> & { id: null };
