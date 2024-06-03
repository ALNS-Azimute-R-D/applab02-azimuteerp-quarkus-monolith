import { ITypeOfPerson, NewTypeOfPerson } from './type-of-person.model';

export const sampleWithRequiredData: ITypeOfPerson = {
  id: 13952,
  code: 'dame ',
  description: 'furthermore round',
};

export const sampleWithPartialData: ITypeOfPerson = {
  id: 11623,
  code: 'patie',
  description: 'grubby adventurously yippee',
};

export const sampleWithFullData: ITypeOfPerson = {
  id: 12278,
  code: 'eek w',
  description: 'tarragon considering tractor',
};

export const sampleWithNewData: NewTypeOfPerson = {
  code: 'light',
  description: 'lined slurp',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
