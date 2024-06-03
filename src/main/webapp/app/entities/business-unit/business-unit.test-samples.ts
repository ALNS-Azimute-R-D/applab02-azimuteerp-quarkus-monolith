import { IBusinessUnit, NewBusinessUnit } from './business-unit.model';

export const sampleWithRequiredData: IBusinessUnit = {
  id: 1072,
  acronym: 'besides who',
  name: 'ack superior',
  activationStatus: 'INVALID',
};

export const sampleWithPartialData: IBusinessUnit = {
  id: 28956,
  acronym: 'likely official atop',
  hierarchicalLevel: 'venerate',
  name: 'anxiously',
  activationStatus: 'INACTIVE',
};

export const sampleWithFullData: IBusinessUnit = {
  id: 30438,
  acronym: 'so',
  hierarchicalLevel: 'surprisingly',
  name: 'um',
  activationStatus: 'INVALID',
};

export const sampleWithNewData: NewBusinessUnit = {
  acronym: 'bus annually',
  name: 'while however gift',
  activationStatus: 'PENDENT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
