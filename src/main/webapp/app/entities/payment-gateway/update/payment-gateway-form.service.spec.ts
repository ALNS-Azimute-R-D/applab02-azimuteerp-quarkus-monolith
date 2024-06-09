import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../payment-gateway.test-samples';

import { PaymentGatewayFormService } from './payment-gateway-form.service';

describe('PaymentGateway Form Service', () => {
  let service: PaymentGatewayFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentGatewayFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentGatewayFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentGatewayFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            aliasCode: expect.any(Object),
            description: expect.any(Object),
            businessHandlerClazz: expect.any(Object),
            activationStatus: expect.any(Object),
          }),
        );
      });

      it('passing IPaymentGateway should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentGatewayFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            aliasCode: expect.any(Object),
            description: expect.any(Object),
            businessHandlerClazz: expect.any(Object),
            activationStatus: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaymentGateway', () => {
      it('should return NewPaymentGateway for default PaymentGateway initial value', () => {
        const formGroup = service.createPaymentGatewayFormGroup(sampleWithNewData);

        const paymentGateway = service.getPaymentGateway(formGroup) as any;

        expect(paymentGateway).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaymentGateway for empty PaymentGateway initial value', () => {
        const formGroup = service.createPaymentGatewayFormGroup();

        const paymentGateway = service.getPaymentGateway(formGroup) as any;

        expect(paymentGateway).toMatchObject({});
      });

      it('should return IPaymentGateway', () => {
        const formGroup = service.createPaymentGatewayFormGroup(sampleWithRequiredData);

        const paymentGateway = service.getPaymentGateway(formGroup) as any;

        expect(paymentGateway).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaymentGateway should not enable id FormControl', () => {
        const formGroup = service.createPaymentGatewayFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaymentGateway should disable id FormControl', () => {
        const formGroup = service.createPaymentGatewayFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
