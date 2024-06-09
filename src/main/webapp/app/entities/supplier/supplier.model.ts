import { IPerson } from 'app/entities/person/person.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface ISupplier {
  id: number;
  acronym?: string | null;
  companyName?: string | null;
  streetAddress?: string | null;
  houseNumber?: string | null;
  locationName?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  zipPostalCode?: string | null;
  countryRegion?: string | null;
  pointLocation?: string | null;
  pointLocationContentType?: string | null;
  mainEmail?: string | null;
  phoneNumber1?: string | null;
  phoneNumber2?: string | null;
  customAttributesDetailsJSON?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  representativePerson?: Pick<IPerson, 'id' | 'lastname'> | null;
}

export type NewSupplier = Omit<ISupplier, 'id'> & { id: null };
