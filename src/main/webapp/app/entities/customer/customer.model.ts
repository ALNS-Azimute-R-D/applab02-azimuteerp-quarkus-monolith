import { ICustomerType } from 'app/entities/customer-type/customer-type.model';
import { IDistrict } from 'app/entities/district/district.model';
import { CustomerStatusEnum } from 'app/entities/enumerations/customer-status-enum.model';

export interface ICustomer {
  id: number;
  customerBusinessCode?: string | null;
  name?: string | null;
  description?: string | null;
  email?: string | null;
  addressDetails?: string | null;
  zipCode?: string | null;
  keycloakGroupDetails?: string | null;
  status?: keyof typeof CustomerStatusEnum | null;
  active?: boolean | null;
  customerType?: Pick<ICustomerType, 'id' | 'name'> | null;
  district?: Pick<IDistrict, 'id' | 'name'> | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
