import { IAssetType } from 'app/entities/asset-type/asset-type.model';
import { StatusRawProcessingEnum } from 'app/entities/enumerations/status-raw-processing-enum.model';

export interface IRawAssetProcTmp {
  id: number;
  name?: string | null;
  statusRawProcessing?: keyof typeof StatusRawProcessingEnum | null;
  fullFilenamePath?: string | null;
  assetRawContentAsBlob?: string | null;
  assetRawContentAsBlobContentType?: string | null;
  extraDetails?: string | null;
  assetType?: Pick<IAssetType, 'id' | 'name'> | null;
}

export type NewRawAssetProcTmp = Omit<IRawAssetProcTmp, 'id'> & { id: null };
