import { IOrganization } from 'app/entities/organization/organization.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IBusinessUnit {
  id: number;
  acronym?: string | null;
  hierarchicalLevel?: string | null;
  name?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  organization?: Pick<IOrganization, 'id' | 'name'> | null;
  businessUnitParent?: Pick<IBusinessUnit, 'id' | 'name'> | null;
}

export type NewBusinessUnit = Omit<IBusinessUnit, 'id'> & { id: null };
