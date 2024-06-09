import { IPaymentGateway, NewPaymentGateway } from './payment-gateway.model';

export const sampleWithRequiredData: IPaymentGateway = {
  id: 20685,
  aliasCode: 'fooey immediately an',
  description: 'sympathetically barring punctually',
  activationStatus: 'BLOCKED',
};

export const sampleWithPartialData: IPaymentGateway = {
  id: 18420,
  aliasCode: 'arrive',
  description: 'sleepily',
  activationStatus: 'ACTIVE',
};

export const sampleWithFullData: IPaymentGateway = {
  id: 29961,
  aliasCode: 'treaty',
  description: 'of before as',
  businessHandlerClazz: 'super',
  activationStatus: 'INVALID',
};

export const sampleWithNewData: NewPaymentGateway = {
  aliasCode: 'properly above up',
  description: 'minute',
  activationStatus: 'INACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
