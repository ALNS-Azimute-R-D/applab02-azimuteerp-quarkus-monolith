import dayjs from 'dayjs/esm';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { InvoiceStatusEnum } from 'app/entities/enumerations/invoice-status-enum.model';

export interface IInvoice {
  id: number;
  businessCode?: string | null;
  originalOrderId?: number | null;
  invoiceDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  description?: string | null;
  taxValue?: number | null;
  shippingValue?: number | null;
  amountDueValue?: number | null;
  numberOfInstallmentsOriginal?: number | null;
  numberOfInstallmentsPaid?: number | null;
  amountPaidValue?: number | null;
  status?: keyof typeof InvoiceStatusEnum | null;
  extraDetails?: string | null;
  preferrablePaymentMethod?: Pick<IPaymentMethod, 'id' | 'code'> | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
