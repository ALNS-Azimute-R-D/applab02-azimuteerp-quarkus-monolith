import dayjs from 'dayjs/esm';

import { IPerson, NewPerson } from './person.model';

export const sampleWithRequiredData: IPerson = {
  id: 4486,
  firstName: 'Warren',
  lastName: 'Kub',
  birthDate: dayjs('2024-06-02'),
  gender: 'OTHER',
  streetAddress: 'even',
  postalCode: 'failing',
  mainEmail: 'qUL1/@(.2_',
  activationStatus: 'INACTIVE',
};

export const sampleWithPartialData: IPerson = {
  id: 1402,
  firstName: 'Brad',
  lastName: 'Casper',
  birthDate: dayjs('2024-06-03'),
  gender: 'FEMALE',
  streetAddress: 'yum crewmen rhapsodise',
  houseNumber: 'ptarmigan briskly',
  postalCode: 'nice',
  mainEmail: 'ceuYv@MEK.BEA+',
  mobilePhoneNumber: 'around only',
  preferredLanguage: 'slowl',
  extraDetails: '../fake-data/blob/hipster.txt',
  activationStatus: 'INACTIVE',
  avatarImg: '../fake-data/blob/hipster.png',
  avatarImgContentType: 'unknown',
};

export const sampleWithFullData: IPerson = {
  id: 29798,
  firstName: 'Craig',
  lastName: 'Wilderman',
  birthDate: dayjs('2024-06-03'),
  gender: 'FEMALE',
  codeBI: 'schooner rapidly org',
  codeNIF: 'consequently',
  streetAddress: 'boo',
  houseNumber: 'needily extreme',
  locationName: 'selfishly gigantic brr',
  postalCode: 'ew condon',
  mainEmail: 'rsrk{@~6.Wh<Y]x',
  landPhoneNumber: 'powerful or nea',
  mobilePhoneNumber: 'however',
  occupation: 'ancient bonsai soak',
  preferredLanguage: 'dodge',
  usernameInOAuth2: 'barring in',
  userIdInOAuth2: 'or feminine what',
  extraDetails: '../fake-data/blob/hipster.txt',
  activationStatus: 'ACTIVE',
  avatarImg: '../fake-data/blob/hipster.png',
  avatarImgContentType: 'unknown',
};

export const sampleWithNewData: NewPerson = {
  firstName: 'Jaclyn',
  lastName: 'Hammes',
  birthDate: dayjs('2024-06-03'),
  gender: 'FEMALE',
  streetAddress: 'fooey rapidly',
  postalCode: 'forenenst',
  mainEmail: '851^7@]w7vo].~_j',
  activationStatus: 'INVALID',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
