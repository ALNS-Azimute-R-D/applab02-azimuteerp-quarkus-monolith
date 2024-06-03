import { IOrganization } from 'app/entities/organization/organization.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IOrganizationRole {
  id: number;
  roleName?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  organization?: Pick<IOrganization, 'id' | 'name'> | null;
}

export type NewOrganizationRole = Omit<IOrganizationRole, 'id'> & { id: null };
