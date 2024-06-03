import dayjs from 'dayjs/esm';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentTypeEnum } from 'app/entities/enumerations/payment-type-enum.model';
import { PaymentStatusEnum } from 'app/entities/enumerations/payment-status-enum.model';

export interface IPayment {
  id: number;
  installmentNumber?: number | null;
  paymentDueDate?: dayjs.Dayjs | null;
  paymentPaidDate?: dayjs.Dayjs | null;
  paymentAmount?: number | null;
  typeOfPayment?: keyof typeof PaymentTypeEnum | null;
  status?: keyof typeof PaymentStatusEnum | null;
  extraDetails?: string | null;
  paymentMethod?: Pick<IPaymentMethod, 'id' | 'code'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
