import dayjs from 'dayjs/esm';

import { IOrganizationMemberRole, NewOrganizationMemberRole } from './organization-member-role.model';

export const sampleWithRequiredData: IOrganizationMemberRole = {
  id: 2491,
  joinedAt: dayjs('2024-06-07'),
};

export const sampleWithPartialData: IOrganizationMemberRole = {
  id: 8069,
  joinedAt: dayjs('2024-06-07'),
};

export const sampleWithFullData: IOrganizationMemberRole = {
  id: 22428,
  joinedAt: dayjs('2024-06-07'),
};

export const sampleWithNewData: NewOrganizationMemberRole = {
  joinedAt: dayjs('2024-06-08'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
