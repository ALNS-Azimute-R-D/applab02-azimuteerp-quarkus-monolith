import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../business-unit.test-samples';

import { BusinessUnitFormService } from './business-unit-form.service';

describe('BusinessUnit Form Service', () => {
  let service: BusinessUnitFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusinessUnitFormService);
  });

  describe('Service methods', () => {
    describe('createBusinessUnitFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBusinessUnitFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            hierarchicalLevel: expect.any(Object),
            name: expect.any(Object),
            activationStatus: expect.any(Object),
            organization: expect.any(Object),
            businessUnitParent: expect.any(Object),
          }),
        );
      });

      it('passing IBusinessUnit should create a new form with FormGroup', () => {
        const formGroup = service.createBusinessUnitFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            hierarchicalLevel: expect.any(Object),
            name: expect.any(Object),
            activationStatus: expect.any(Object),
            organization: expect.any(Object),
            businessUnitParent: expect.any(Object),
          }),
        );
      });
    });

    describe('getBusinessUnit', () => {
      it('should return NewBusinessUnit for default BusinessUnit initial value', () => {
        const formGroup = service.createBusinessUnitFormGroup(sampleWithNewData);

        const businessUnit = service.getBusinessUnit(formGroup) as any;

        expect(businessUnit).toMatchObject(sampleWithNewData);
      });

      it('should return NewBusinessUnit for empty BusinessUnit initial value', () => {
        const formGroup = service.createBusinessUnitFormGroup();

        const businessUnit = service.getBusinessUnit(formGroup) as any;

        expect(businessUnit).toMatchObject({});
      });

      it('should return IBusinessUnit', () => {
        const formGroup = service.createBusinessUnitFormGroup(sampleWithRequiredData);

        const businessUnit = service.getBusinessUnit(formGroup) as any;

        expect(businessUnit).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBusinessUnit should not enable id FormControl', () => {
        const formGroup = service.createBusinessUnitFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBusinessUnit should disable id FormControl', () => {
        const formGroup = service.createBusinessUnitFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
