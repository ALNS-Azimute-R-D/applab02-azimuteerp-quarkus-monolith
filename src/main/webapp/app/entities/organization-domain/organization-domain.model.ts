import { IOrganization } from 'app/entities/organization/organization.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IOrganizationDomain {
  id: number;
  domainAcronym?: string | null;
  name?: string | null;
  isVerified?: boolean | null;
  businessHandlerClazz?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  organization?: Pick<IOrganization, 'id' | 'name'> | null;
}

export type NewOrganizationDomain = Omit<IOrganizationDomain, 'id'> & { id: null };
