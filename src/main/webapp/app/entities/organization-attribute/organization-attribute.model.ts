import { IOrganization } from 'app/entities/organization/organization.model';

export interface IOrganizationAttribute {
  id: number;
  attributeName?: string | null;
  attributeValue?: string | null;
  organization?: Pick<IOrganization, 'id' | 'name'> | null;
}

export type NewOrganizationAttribute = Omit<IOrganizationAttribute, 'id'> & { id: null };
