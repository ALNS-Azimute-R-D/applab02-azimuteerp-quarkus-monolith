import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../organization-attribute.test-samples';

import { OrganizationAttributeFormService } from './organization-attribute-form.service';

describe('OrganizationAttribute Form Service', () => {
  let service: OrganizationAttributeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganizationAttributeFormService);
  });

  describe('Service methods', () => {
    describe('createOrganizationAttributeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrganizationAttributeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attributeName: expect.any(Object),
            attributeValue: expect.any(Object),
            organization: expect.any(Object),
          }),
        );
      });

      it('passing IOrganizationAttribute should create a new form with FormGroup', () => {
        const formGroup = service.createOrganizationAttributeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attributeName: expect.any(Object),
            attributeValue: expect.any(Object),
            organization: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrganizationAttribute', () => {
      it('should return NewOrganizationAttribute for default OrganizationAttribute initial value', () => {
        const formGroup = service.createOrganizationAttributeFormGroup(sampleWithNewData);

        const organizationAttribute = service.getOrganizationAttribute(formGroup) as any;

        expect(organizationAttribute).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrganizationAttribute for empty OrganizationAttribute initial value', () => {
        const formGroup = service.createOrganizationAttributeFormGroup();

        const organizationAttribute = service.getOrganizationAttribute(formGroup) as any;

        expect(organizationAttribute).toMatchObject({});
      });

      it('should return IOrganizationAttribute', () => {
        const formGroup = service.createOrganizationAttributeFormGroup(sampleWithRequiredData);

        const organizationAttribute = service.getOrganizationAttribute(formGroup) as any;

        expect(organizationAttribute).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrganizationAttribute should not enable id FormControl', () => {
        const formGroup = service.createOrganizationAttributeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrganizationAttribute should disable id FormControl', () => {
        const formGroup = service.createOrganizationAttributeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
