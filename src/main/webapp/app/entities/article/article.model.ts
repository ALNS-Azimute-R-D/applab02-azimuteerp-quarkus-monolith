import { ICategory } from 'app/entities/category/category.model';
import { SizeOptionEnum } from 'app/entities/enumerations/size-option-enum.model';

export interface IArticle {
  id: number;
  inventoryProductId?: number | null;
  customName?: string | null;
  customDescription?: string | null;
  priceValue?: number | null;
  itemSize?: keyof typeof SizeOptionEnum | null;
  assetsCollectionUUID?: string | null;
  isEnabled?: boolean | null;
  mainCategory?: Pick<ICategory, 'id' | 'acronym'> | null;
}

export type NewArticle = Omit<IArticle, 'id'> & { id: null };
