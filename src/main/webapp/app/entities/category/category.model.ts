export interface ICategory {
  id: number;
  acronym?: string | null;
  name?: string | null;
  description?: string | null;
  handlerClazzName?: string | null;
  categoryParent?: Pick<ICategory, 'id' | 'acronym'> | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
