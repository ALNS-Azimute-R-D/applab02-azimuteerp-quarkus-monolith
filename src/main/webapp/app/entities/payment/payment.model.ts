import dayjs from 'dayjs/esm';
import { IPaymentGateway } from 'app/entities/payment-gateway/payment-gateway.model';
import { PaymentTypeEnum } from 'app/entities/enumerations/payment-type-enum.model';
import { PaymentStatusEnum } from 'app/entities/enumerations/payment-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IPayment {
  id: number;
  installmentNumber?: number | null;
  paymentDueDate?: dayjs.Dayjs | null;
  paymentPaidDate?: dayjs.Dayjs | null;
  paymentAmount?: number | null;
  typeOfPayment?: keyof typeof PaymentTypeEnum | null;
  statusPayment?: keyof typeof PaymentStatusEnum | null;
  customAttributesDetailsJSON?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  paymentGateway?: Pick<IPaymentGateway, 'id' | 'aliasCode'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
