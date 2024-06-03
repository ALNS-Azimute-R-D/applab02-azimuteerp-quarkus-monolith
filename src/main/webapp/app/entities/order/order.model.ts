import dayjs from 'dayjs/esm';
import { OrderStatusEnum } from 'app/entities/enumerations/order-status-enum.model';

export interface IOrder {
  id: number;
  businessCode?: string | null;
  customerUserId?: string | null;
  placedDate?: dayjs.Dayjs | null;
  totalTaxValue?: number | null;
  totalDueValue?: number | null;
  status?: keyof typeof OrderStatusEnum | null;
  invoiceId?: number | null;
  estimatedDeliveryDate?: dayjs.Dayjs | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
