export interface IPaymentMethod {
  id: number;
  code?: string | null;
  description?: string | null;
  businessHandlerClazz?: string | null;
}

export type NewPaymentMethod = Omit<IPaymentMethod, 'id'> & { id: null };
