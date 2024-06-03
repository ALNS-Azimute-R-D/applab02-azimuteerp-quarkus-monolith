import { IRawAssetProcTmp, NewRawAssetProcTmp } from './raw-asset-proc-tmp.model';

export const sampleWithRequiredData: IRawAssetProcTmp = {
  id: 7328,
  name: 'concede although necessary',
};

export const sampleWithPartialData: IRawAssetProcTmp = {
  id: 30666,
  name: 'err reunion',
  statusRawProcessing: 'DONE',
  assetRawContentAsBlob: '../fake-data/blob/hipster.png',
  assetRawContentAsBlobContentType: 'unknown',
};

export const sampleWithFullData: IRawAssetProcTmp = {
  id: 9274,
  name: 'emanate',
  statusRawProcessing: 'UPLOADED',
  fullFilenamePath: 'minus',
  assetRawContentAsBlob: '../fake-data/blob/hipster.png',
  assetRawContentAsBlobContentType: 'unknown',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewRawAssetProcTmp = {
  name: 'justify',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
