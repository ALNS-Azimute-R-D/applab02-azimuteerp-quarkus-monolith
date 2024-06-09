import dayjs from 'dayjs/esm';
import { IPaymentGateway } from 'app/entities/payment-gateway/payment-gateway.model';
import { InvoiceStatusEnum } from 'app/entities/enumerations/invoice-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IInvoice {
  id: number;
  businessCode?: string | null;
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
  customAttributesDetailsJSON?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  preferrablePaymentGateway?: Pick<IPaymentGateway, 'id' | 'aliasCode'> | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
