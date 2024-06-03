import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../customer-type.test-samples';

import { CustomerTypeFormService } from './customer-type-form.service';

describe('CustomerType Form Service', () => {
  let service: CustomerTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerTypeFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            businessHandlerClazz: expect.any(Object),
          }),
        );
      });

      it('passing ICustomerType should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            businessHandlerClazz: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomerType', () => {
      it('should return NewCustomerType for default CustomerType initial value', () => {
        const formGroup = service.createCustomerTypeFormGroup(sampleWithNewData);

        const customerType = service.getCustomerType(formGroup) as any;

        expect(customerType).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomerType for empty CustomerType initial value', () => {
        const formGroup = service.createCustomerTypeFormGroup();

        const customerType = service.getCustomerType(formGroup) as any;

        expect(customerType).toMatchObject({});
      });

      it('should return ICustomerType', () => {
        const formGroup = service.createCustomerTypeFormGroup(sampleWithRequiredData);

        const customerType = service.getCustomerType(formGroup) as any;

        expect(customerType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomerType should not enable id FormControl', () => {
        const formGroup = service.createCustomerTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomerType should disable id FormControl', () => {
        const formGroup = service.createCustomerTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
