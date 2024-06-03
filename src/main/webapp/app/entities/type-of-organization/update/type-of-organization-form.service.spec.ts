import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-of-organization.test-samples';

import { TypeOfOrganizationFormService } from './type-of-organization-form.service';

describe('TypeOfOrganization Form Service', () => {
  let service: TypeOfOrganizationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeOfOrganizationFormService);
  });

  describe('Service methods', () => {
    describe('createTypeOfOrganizationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeOfOrganizationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            businessHandlerClazz: expect.any(Object),
          }),
        );
      });

      it('passing ITypeOfOrganization should create a new form with FormGroup', () => {
        const formGroup = service.createTypeOfOrganizationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            acronym: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            businessHandlerClazz: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeOfOrganization', () => {
      it('should return NewTypeOfOrganization for default TypeOfOrganization initial value', () => {
        const formGroup = service.createTypeOfOrganizationFormGroup(sampleWithNewData);

        const typeOfOrganization = service.getTypeOfOrganization(formGroup) as any;

        expect(typeOfOrganization).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeOfOrganization for empty TypeOfOrganization initial value', () => {
        const formGroup = service.createTypeOfOrganizationFormGroup();

        const typeOfOrganization = service.getTypeOfOrganization(formGroup) as any;

        expect(typeOfOrganization).toMatchObject({});
      });

      it('should return ITypeOfOrganization', () => {
        const formGroup = service.createTypeOfOrganizationFormGroup(sampleWithRequiredData);

        const typeOfOrganization = service.getTypeOfOrganization(formGroup) as any;

        expect(typeOfOrganization).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeOfOrganization should not enable id FormControl', () => {
        const formGroup = service.createTypeOfOrganizationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeOfOrganization should disable id FormControl', () => {
        const formGroup = service.createTypeOfOrganizationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
