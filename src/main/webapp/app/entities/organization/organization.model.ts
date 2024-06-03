import { ITenant } from 'app/entities/tenant/tenant.model';
import { ITypeOfOrganization } from 'app/entities/type-of-organization/type-of-organization.model';
import { OrganizationStatusEnum } from 'app/entities/enumerations/organization-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IOrganization {
  id: number;
  acronym?: string | null;
  businessCode?: string | null;
  hierarchicalLevel?: string | null;
  name?: string | null;
  description?: string | null;
  businessHandlerClazz?: string | null;
  mainContactPersonDetails?: string | null;
  technicalEnvironmentsDetails?: string | null;
  commonCustomAttributesDetails?: string | null;
  organizationStatus?: keyof typeof OrganizationStatusEnum | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  logoImg?: string | null;
  logoImgContentType?: string | null;
  tenant?: Pick<ITenant, 'id' | 'name'> | null;
  typeOfOrganization?: Pick<ITypeOfOrganization, 'id' | 'name'> | null;
  organizationParent?: Pick<IOrganization, 'id' | 'name'> | null;
}

export type NewOrganization = Omit<IOrganization, 'id'> & { id: null };
