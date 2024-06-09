import { IAssetCollection } from 'app/entities/asset-collection/asset-collection.model';
import { ICategory } from 'app/entities/category/category.model';
import { SizeOptionEnum } from 'app/entities/enumerations/size-option-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IArticle {
  id: number;
  inventoryProductId?: number | null;
  skuCode?: string | null;
  customName?: string | null;
  customDescription?: string | null;
  priceValue?: number | null;
  itemSize?: keyof typeof SizeOptionEnum | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  assetCollections?: Pick<IAssetCollection, 'id'>[] | null;
  mainCategory?: Pick<ICategory, 'id' | 'acronym'> | null;
}

export type NewArticle = Omit<IArticle, 'id'> & { id: null };
