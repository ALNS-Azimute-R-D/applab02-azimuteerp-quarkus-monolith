import { IWarehouse, NewWarehouse } from './warehouse.model';

export const sampleWithRequiredData: IWarehouse = {
  id: 16254,
  acronym: 'between',
  name: 'than',
  streetAddress: 'preside',
  postalCode: 'fooey tha',
};

export const sampleWithPartialData: IWarehouse = {
  id: 14314,
  acronym: 'questioningly interestingly pro',
  name: 'which yippee',
  streetAddress: 'through',
  locationName: 'fooey possible',
  postalCode: 'dimly',
  pointLocation: '../fake-data/blob/hipster.png',
  pointLocationContentType: 'unknown',
  mainEmail: '&DoCu@I!f.\\UW)',
};

export const sampleWithFullData: IWarehouse = {
  id: 29190,
  acronym: 'near',
  name: 'although vex indeed',
  description: '../fake-data/blob/hipster.txt',
  streetAddress: 'nervously',
  houseNumber: 'um',
  locationName: 'willfully vicious',
  postalCode: 'boot',
  pointLocation: '../fake-data/blob/hipster.png',
  pointLocationContentType: 'unknown',
  mainEmail: 'YriLvY@Nl.h_3(HG',
  landPhoneNumber: 'meh platform',
  mobilePhoneNumber: 'unconscious gym',
  faxNumber: 'beside dearly',
  extraDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewWarehouse = {
  acronym: 'digital girdle',
  name: 'furthermore',
  streetAddress: 'worst nor sparse',
  postalCode: 'whole pho',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
