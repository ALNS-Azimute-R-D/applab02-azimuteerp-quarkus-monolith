import { ISupplier, NewSupplier } from './supplier.model';

export const sampleWithRequiredData: ISupplier = {
  id: 5042,
  acronym: 'staircase blah',
  companyName: 'whoa',
  streetAddress: 'aw politely',
};

export const sampleWithPartialData: ISupplier = {
  id: 29592,
  acronym: 'whether knowingly',
  companyName: 'how bestir',
  representativeLastName: 'incidentally',
  representativeFirstName: 'freely',
  streetAddress: 'likewise during lava',
  houseNumber: 'feline meh',
  zipPostalCode: 'sometimes appro',
  countryRegion: 'gawp dimly',
  landPhoneNumber: 'geez',
  faxNumber: 'outside',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ISupplier = {
  id: 13907,
  acronym: 'gallivant gleaming',
  companyName: 'openly',
  representativeLastName: 'prison meanwhile before',
  representativeFirstName: 'active',
  jobTitle: 'Central Data Producer',
  streetAddress: 'indolent',
  houseNumber: 'whose upwardly',
  locationName: 'icing aside',
  city: 'North Deloresstead',
  stateProvince: 'cheery whether',
  zipPostalCode: 'ulcerate',
  countryRegion: 'instead guideline yet',
  webPage: '../fake-data/blob/hipster.txt',
  pointLocation: '../fake-data/blob/hipster.png',
  pointLocationContentType: 'unknown',
  mainEmail: '!]Q6Y#@e&sA#b.oH#',
  landPhoneNumber: 'harness amidst ',
  mobilePhoneNumber: 'woot gurgle ast',
  faxNumber: 'whenever',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewSupplier = {
  acronym: 'judgementally in',
  companyName: 'whose',
  streetAddress: 'look befit sand',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
