import { IOrganizationRole, NewOrganizationRole } from './organization-role.model';

export const sampleWithRequiredData: IOrganizationRole = {
  id: 29864,
  roleName: 'yum hot troubled',
  activationStatus: 'ON_HOLD',
};

export const sampleWithPartialData: IOrganizationRole = {
  id: 1943,
  roleName: 'how whether polish',
  activationStatus: 'INACTIVE',
};

export const sampleWithFullData: IOrganizationRole = {
  id: 26806,
  roleName: 'gracious acidly ew',
  activationStatus: 'ON_HOLD',
};

export const sampleWithNewData: NewOrganizationRole = {
  roleName: 'whoever lacquerware',
  activationStatus: 'ON_HOLD',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
