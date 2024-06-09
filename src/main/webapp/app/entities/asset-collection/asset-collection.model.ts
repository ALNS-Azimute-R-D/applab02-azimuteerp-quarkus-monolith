import { IAsset } from 'app/entities/asset/asset.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IAssetCollection {
  id: number;
  name?: string | null;
  fullFilenamePath?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  assets?: Pick<IAsset, 'id'>[] | null;
}

export type NewAssetCollection = Omit<IAssetCollection, 'id'> & { id: null };
