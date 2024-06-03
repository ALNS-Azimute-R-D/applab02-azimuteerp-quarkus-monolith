import dayjs from 'dayjs/esm';
import { ITypeOfPerson } from 'app/entities/type-of-person/type-of-person.model';
import { IDistrict } from 'app/entities/district/district.model';
import { GenderEnum } from 'app/entities/enumerations/gender-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IPerson {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  gender?: keyof typeof GenderEnum | null;
  codeBI?: string | null;
  codeNIF?: string | null;
  streetAddress?: string | null;
  houseNumber?: string | null;
  locationName?: string | null;
  postalCode?: string | null;
  mainEmail?: string | null;
  landPhoneNumber?: string | null;
  mobilePhoneNumber?: string | null;
  occupation?: string | null;
  preferredLanguage?: string | null;
  usernameInOAuth2?: string | null;
  userIdInOAuth2?: string | null;
  extraDetails?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  avatarImg?: string | null;
  avatarImgContentType?: string | null;
  typeOfPerson?: Pick<ITypeOfPerson, 'id' | 'code'> | null;
  district?: Pick<IDistrict, 'id' | 'name'> | null;
  managerPerson?: Pick<IPerson, 'id' | 'lastName'> | null;
}

export type NewPerson = Omit<IPerson, 'id'> & { id: null };
