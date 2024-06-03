import dayjs from 'dayjs/esm';
import { IOrganization } from 'app/entities/organization/organization.model';
import { IPerson } from 'app/entities/person/person.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IOrganizationMembership {
  id: number;
  joinedAt?: dayjs.Dayjs | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  organization?: Pick<IOrganization, 'id' | 'name'> | null;
  person?: Pick<IPerson, 'id' | 'lastName'> | null;
}

export type NewOrganizationMembership = Omit<IOrganizationMembership, 'id'> & { id: null };
