export interface ITypeOfPerson {
  id: number;
  code?: string | null;
  description?: string | null;
}

export type NewTypeOfPerson = Omit<ITypeOfPerson, 'id'> & { id: null };
