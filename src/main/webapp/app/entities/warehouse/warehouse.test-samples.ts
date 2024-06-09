import { IWarehouse, NewWarehouse } from './warehouse.model';

export const sampleWithRequiredData: IWarehouse = {
  id: 16254,
  acronym: 'between',
  name: 'than',
  streetAddress: 'preside',
  postalCode: 'fooey tha',
  activationStatus: 'PENDENT',
};

export const sampleWithPartialData: IWarehouse = {
  id: 26104,
  acronym: 'versus officially flit',
  name: 'though chassis through',
  streetAddress: 'fooey possible',
  houseNumber: 'dimly',
  locationName: 'woot bonk frizzy',
  postalCode: 'fervently',
  pointLocation: '../fake-data/blob/hipster.png',
  pointLocationContentType: 'unknown',
  customAttributesDetailsJSON: 'seemingly tout bad',
  activationStatus: 'BLOCKED',
};

export const sampleWithFullData: IWarehouse = {
  id: 19613,
  acronym: 'corrode or puzzled',
  name: 'vainly',
  description: 'difficult sputter',
  streetAddress: 'oh stretcher',
  houseNumber: 'or petition given',
  locationName: 'indeed lest whoa',
  postalCode: 'foolishly',
  pointLocation: '../fake-data/blob/hipster.png',
  pointLocationContentType: 'unknown',
  mainEmail: 'kok}0@5l(mM/.tkbVjb',
  landPhoneNumber: 'astrologer cell',
  mobilePhoneNumber: 'till',
  faxNumber: 'soil um',
  customAttributesDetailsJSON: 'ha brand clue',
  activationStatus: 'INACTIVE',
};

export const sampleWithNewData: NewWarehouse = {
  acronym: 'generally yowza',
  name: 'meanwhile',
  streetAddress: 'tomato yum',
  postalCode: 'stock apu',
  activationStatus: 'INVALID',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
