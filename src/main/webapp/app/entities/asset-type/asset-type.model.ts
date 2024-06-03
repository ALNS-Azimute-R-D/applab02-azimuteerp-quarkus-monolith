export interface IAssetType {
  id: number;
  acronym?: string | null;
  name?: string | null;
  description?: string | null;
  handlerClazzName?: string | null;
  extraDetails?: string | null;
}

export type NewAssetType = Omit<IAssetType, 'id'> & { id: null };
