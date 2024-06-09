import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IPaymentGateway {
  id: number;
  aliasCode?: string | null;
  description?: string | null;
  businessHandlerClazz?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
}

export type NewPaymentGateway = Omit<IPaymentGateway, 'id'> & { id: null };
