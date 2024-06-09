import dayjs from 'dayjs/esm';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { OrderStatusEnum } from 'app/entities/enumerations/order-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IOrder {
  id: number;
  businessCode?: string | null;
  placedDate?: dayjs.Dayjs | null;
  totalTaxValue?: number | null;
  totalDueValue?: number | null;
  status?: keyof typeof OrderStatusEnum | null;
  estimatedDeliveryDate?: dayjs.Dayjs | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  invoice?: Pick<IInvoice, 'id' | 'description'> | null;
  customer?: Pick<ICustomer, 'id' | 'fullname'> | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
