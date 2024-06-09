import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface ITenant {
  id: number;
  acronym?: string | null;
  name?: string | null;
  description?: string | null;
  customerBusinessCode?: string | null;
  businessHandlerClazz?: string | null;
  mainContactPersonDetailsJSON?: string | null;
  billingDetailsJSON?: string | null;
  technicalEnvironmentsDetailsJSON?: string | null;
  customAttributesDetailsJSON?: string | null;
  logoImg?: string | null;
  logoImgContentType?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
}

export type NewTenant = Omit<ITenant, 'id'> & { id: null };
