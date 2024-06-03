import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../organization-role.test-samples';

import { OrganizationRoleFormService } from './organization-role-form.service';

describe('OrganizationRole Form Service', () => {
  let service: OrganizationRoleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganizationRoleFormService);
  });

  describe('Service methods', () => {
    describe('createOrganizationRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrganizationRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleName: expect.any(Object),
            activationStatus: expect.any(Object),
            organization: expect.any(Object),
          }),
        );
      });

      it('passing IOrganizationRole should create a new form with FormGroup', () => {
        const formGroup = service.createOrganizationRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleName: expect.any(Object),
            activationStatus: expect.any(Object),
            organization: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrganizationRole', () => {
      it('should return NewOrganizationRole for default OrganizationRole initial value', () => {
        const formGroup = service.createOrganizationRoleFormGroup(sampleWithNewData);

        const organizationRole = service.getOrganizationRole(formGroup) as any;

        expect(organizationRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrganizationRole for empty OrganizationRole initial value', () => {
        const formGroup = service.createOrganizationRoleFormGroup();

        const organizationRole = service.getOrganizationRole(formGroup) as any;

        expect(organizationRole).toMatchObject({});
      });

      it('should return IOrganizationRole', () => {
        const formGroup = service.createOrganizationRoleFormGroup(sampleWithRequiredData);

        const organizationRole = service.getOrganizationRole(formGroup) as any;

        expect(organizationRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrganizationRole should not enable id FormControl', () => {
        const formGroup = service.createOrganizationRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrganizationRole should disable id FormControl', () => {
        const formGroup = service.createOrganizationRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
