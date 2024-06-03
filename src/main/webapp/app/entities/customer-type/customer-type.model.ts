export interface ICustomerType {
  id: number;
  name?: string | null;
  description?: string | null;
  businessHandlerClazz?: string | null;
}

export type NewCustomerType = Omit<ICustomerType, 'id'> & { id: null };
