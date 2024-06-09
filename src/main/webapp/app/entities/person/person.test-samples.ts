import dayjs from 'dayjs/esm';

import { IPerson, NewPerson } from './person.model';

export const sampleWithRequiredData: IPerson = {
  id: 4486,
  firstname: 'ugh officially ouch',
  lastname: 'greedily possess yuck',
  birthDate: dayjs('2024-06-07'),
  gender: 'MALE',
  streetAddress: 'gah',
  postalCode: 'arch',
  mainEmail: '^d5%gc@lTg\\kU.v?{c',
  activationStatus: 'INVALID',
};

export const sampleWithPartialData: IPerson = {
  id: 18575,
  firstname: 'loyally buyer offering',
  lastname: 'only',
  birthDate: dayjs('2024-06-07'),
  gender: 'MALE',
  streetAddress: 'strap aside',
  houseNumber: 'for',
  locationName: 'below amid bah',
  postalCode: 'accessory',
  mainEmail: '*]@%.~*`)r*',
  landPhoneNumber: 'needily extreme',
  mobilePhoneNumber: 'selfishly gigan',
  occupation: 'ew condone',
  preferredLanguage: 'yield',
  usernameInOAuth2: 'meh intently',
  activationStatus: 'PENDENT',
};

export const sampleWithFullData: IPerson = {
  id: 1576,
  firstname: 'willfully ancient bonsai',
  lastname: 'bracket',
  fullname: 'bah',
  birthDate: dayjs('2024-06-08'),
  gender: 'FEMALE',
  codeBI: 'qua vague scout',
  codeNIF: 'grubby apud definiti',
  streetAddress: 'bruised matriculate',
  houseNumber: 'delay doubtfully unt',
  locationName: 'loose minister get',
  postalCode: 'for brisk',
  mainEmail: 'NY@e:.>',
  landPhoneNumber: 'eek',
  mobilePhoneNumber: 'hourly',
  occupation: 'epee footwear',
  preferredLanguage: 'super',
  usernameInOAuth2: 'collision lest candidate',
  userIdInOAuth2: 'home than thyme',
  customAttributesDetailsJSON: 'brief',
  activationStatus: 'INVALID',
  avatarImg: '../fake-data/blob/hipster.png',
  avatarImgContentType: 'unknown',
};

export const sampleWithNewData: NewPerson = {
  firstname: 'finally',
  lastname: 'suddenly trumpet curly',
  birthDate: dayjs('2024-06-07'),
  gender: 'FEMALE',
  streetAddress: 'inside upon',
  postalCode: 'underneat',
  mainEmail: 'H@/aP.hrcbUU',
  activationStatus: 'BLOCKED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
