import { IPerson } from 'app/entities/person/person.model';
import { ICustomerType } from 'app/entities/customer-type/customer-type.model';
import { IDistrict } from 'app/entities/district/district.model';
import { CustomerStatusEnum } from 'app/entities/enumerations/customer-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface ICustomer {
  id: number;
  customerBusinessCode?: string | null;
  fullname?: string | null;
  customAttributesDetailsJSON?: string | null;
  customerStatus?: keyof typeof CustomerStatusEnum | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  buyerPerson?: Pick<IPerson, 'id' | 'lastname'> | null;
  customerType?: Pick<ICustomerType, 'id' | 'name'> | null;
  district?: Pick<IDistrict, 'id' | 'name'> | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
