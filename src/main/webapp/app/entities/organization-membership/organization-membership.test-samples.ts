import dayjs from 'dayjs/esm';

import { IOrganizationMembership, NewOrganizationMembership } from './organization-membership.model';

export const sampleWithRequiredData: IOrganizationMembership = {
  id: 23555,
  joinedAt: dayjs('2024-06-08'),
  activationStatus: 'PENDENT',
};

export const sampleWithPartialData: IOrganizationMembership = {
  id: 32578,
  joinedAt: dayjs('2024-06-07'),
  activationStatus: 'INACTIVE',
};

export const sampleWithFullData: IOrganizationMembership = {
  id: 19751,
  joinedAt: dayjs('2024-06-07'),
  activationStatus: 'INVALID',
};

export const sampleWithNewData: NewOrganizationMembership = {
  joinedAt: dayjs('2024-06-07'),
  activationStatus: 'BLOCKED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
