import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 6783,
  name: 'after dragon',
};

export const sampleWithPartialData: ICategory = {
  id: 19036,
  name: 'insert reparation',
  handlerClazzName: 'oxford supportive old',
};

export const sampleWithFullData: ICategory = {
  id: 12553,
  acronym: 'and',
  name: 'heat drat uh-huh',
  description: 'apud adventurously',
  handlerClazzName: 'who kosher toward',
};

export const sampleWithNewData: NewCategory = {
  name: 'outlaw enthuse seemingly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
