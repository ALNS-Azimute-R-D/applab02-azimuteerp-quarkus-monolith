export interface ITypeOfOrganization {
  id: number;
  acronym?: string | null;
  name?: string | null;
  description?: string | null;
  businessHandlerClazz?: string | null;
}

export type NewTypeOfOrganization = Omit<ITypeOfOrganization, 'id'> & { id: null };
