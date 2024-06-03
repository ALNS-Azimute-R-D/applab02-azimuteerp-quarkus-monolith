import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../stock-level.test-samples';

import { StockLevelFormService } from './stock-level-form.service';

describe('StockLevel Form Service', () => {
  let service: StockLevelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StockLevelFormService);
  });

  describe('Service methods', () => {
    describe('createStockLevelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStockLevelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            ramainingQuantity: expect.any(Object),
            extraDetails: expect.any(Object),
            warehouse: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IStockLevel should create a new form with FormGroup', () => {
        const formGroup = service.createStockLevelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            ramainingQuantity: expect.any(Object),
            extraDetails: expect.any(Object),
            warehouse: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getStockLevel', () => {
      it('should return NewStockLevel for default StockLevel initial value', () => {
        const formGroup = service.createStockLevelFormGroup(sampleWithNewData);

        const stockLevel = service.getStockLevel(formGroup) as any;

        expect(stockLevel).toMatchObject(sampleWithNewData);
      });

      it('should return NewStockLevel for empty StockLevel initial value', () => {
        const formGroup = service.createStockLevelFormGroup();

        const stockLevel = service.getStockLevel(formGroup) as any;

        expect(stockLevel).toMatchObject({});
      });

      it('should return IStockLevel', () => {
        const formGroup = service.createStockLevelFormGroup(sampleWithRequiredData);

        const stockLevel = service.getStockLevel(formGroup) as any;

        expect(stockLevel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStockLevel should not enable id FormControl', () => {
        const formGroup = service.createStockLevelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStockLevel should disable id FormControl', () => {
        const formGroup = service.createStockLevelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
