import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inventory-transaction.test-samples';

import { InventoryTransactionFormService } from './inventory-transaction-form.service';

describe('InventoryTransaction Form Service', () => {
  let service: InventoryTransactionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InventoryTransactionFormService);
  });

  describe('Service methods', () => {
    describe('createInventoryTransactionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInventoryTransactionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceId: expect.any(Object),
            transactionCreatedDate: expect.any(Object),
            transactionModifiedDate: expect.any(Object),
            quantity: expect.any(Object),
            transactionComments: expect.any(Object),
            activationStatus: expect.any(Object),
            supplier: expect.any(Object),
            product: expect.any(Object),
            warehouse: expect.any(Object),
          }),
        );
      });

      it('passing IInventoryTransaction should create a new form with FormGroup', () => {
        const formGroup = service.createInventoryTransactionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceId: expect.any(Object),
            transactionCreatedDate: expect.any(Object),
            transactionModifiedDate: expect.any(Object),
            quantity: expect.any(Object),
            transactionComments: expect.any(Object),
            activationStatus: expect.any(Object),
            supplier: expect.any(Object),
            product: expect.any(Object),
            warehouse: expect.any(Object),
          }),
        );
      });
    });

    describe('getInventoryTransaction', () => {
      it('should return NewInventoryTransaction for default InventoryTransaction initial value', () => {
        const formGroup = service.createInventoryTransactionFormGroup(sampleWithNewData);

        const inventoryTransaction = service.getInventoryTransaction(formGroup) as any;

        expect(inventoryTransaction).toMatchObject(sampleWithNewData);
      });

      it('should return NewInventoryTransaction for empty InventoryTransaction initial value', () => {
        const formGroup = service.createInventoryTransactionFormGroup();

        const inventoryTransaction = service.getInventoryTransaction(formGroup) as any;

        expect(inventoryTransaction).toMatchObject({});
      });

      it('should return IInventoryTransaction', () => {
        const formGroup = service.createInventoryTransactionFormGroup(sampleWithRequiredData);

        const inventoryTransaction = service.getInventoryTransaction(formGroup) as any;

        expect(inventoryTransaction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInventoryTransaction should not enable id FormControl', () => {
        const formGroup = service.createInventoryTransactionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInventoryTransaction should disable id FormControl', () => {
        const formGroup = service.createInventoryTransactionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
