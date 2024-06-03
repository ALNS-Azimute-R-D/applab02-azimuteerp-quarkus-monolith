import { IAsset } from 'app/entities/asset/asset.model';

export interface IAssetMetadata {
  id: number;
  metadataDetails?: string | null;
  asset?: Pick<IAsset, 'id' | 'name'> | null;
}

export type NewAssetMetadata = Omit<IAssetMetadata, 'id'> & { id: null };
