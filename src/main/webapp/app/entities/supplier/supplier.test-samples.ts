import { ISupplier, NewSupplier } from './supplier.model';

export const sampleWithRequiredData: ISupplier = {
  id: 5042,
  acronym: 'staircase blah',
  companyName: 'whoa',
  streetAddress: 'aw politely',
  activationStatus: 'ACTIVE',
};

export const sampleWithPartialData: ISupplier = {
  id: 16133,
  acronym: 'apropos pasture',
  companyName: 'suppress sass whether',
  streetAddress: 'aha till ick',
  houseNumber: 'zowie pin',
  city: 'Rebekahborough',
  pointLocation: '../fake-data/blob/hipster.png',
  pointLocationContentType: 'unknown',
  mainEmail: 'f:\\D5g@.Nm>.aa"M',
  activationStatus: 'BLOCKED',
};

export const sampleWithFullData: ISupplier = {
  id: 18223,
  acronym: 'quaintly',
  companyName: 'cluttered',
  streetAddress: 'gosh aboard',
  houseNumber: 'quaintly',
  locationName: 'past',
  city: 'Predovicmouth',
  stateProvince: 'spry',
  zipPostalCode: 'accountant so a',
  countryRegion: 'joshingly',
  pointLocation: '../fake-data/blob/hipster.png',
  pointLocationContentType: 'unknown',
  mainEmail: '*?@QvGI[!.kf4;',
  phoneNumber1: 'eek yum ingredi',
  phoneNumber2: 'very birdbath j',
  customAttributesDetailsJSON: 'classify about lest',
  activationStatus: 'INVALID',
};

export const sampleWithNewData: NewSupplier = {
  acronym: 'whereas decamp inside',
  companyName: 'brr',
  streetAddress: 'between',
  activationStatus: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
