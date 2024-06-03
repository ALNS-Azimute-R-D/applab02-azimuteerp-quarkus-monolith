import dayjs from 'dayjs/esm';
import { IOrganizationMembership } from 'app/entities/organization-membership/organization-membership.model';
import { IOrganizationRole } from 'app/entities/organization-role/organization-role.model';

export interface IOrganizationMemberRole {
  id: number;
  joinedAt?: dayjs.Dayjs | null;
  organizationMembership?: Pick<IOrganizationMembership, 'id'> | null;
  organizationRole?: Pick<IOrganizationRole, 'id'> | null;
}

export type NewOrganizationMemberRole = Omit<IOrganizationMemberRole, 'id'> & { id: null };
