import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

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
  customAttributesDetailsJSON?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
}

export type NewWarehouse = Omit<IWarehouse, 'id'> & { id: null };
