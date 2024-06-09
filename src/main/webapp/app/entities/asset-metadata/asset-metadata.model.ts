import { IAsset } from 'app/entities/asset/asset.model';

export interface IAssetMetadata {
  id: number;
  metadataDetailsJSON?: string | null;
  asset?: Pick<IAsset, 'id' | 'name'> | null;
}

export type NewAssetMetadata = Omit<IAssetMetadata, 'id'> & { id: null };
