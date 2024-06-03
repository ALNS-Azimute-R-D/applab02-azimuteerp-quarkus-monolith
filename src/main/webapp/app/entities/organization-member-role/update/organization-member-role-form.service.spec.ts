import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../organization-member-role.test-samples';

import { OrganizationMemberRoleFormService } from './organization-member-role-form.service';

describe('OrganizationMemberRole Form Service', () => {
  let service: OrganizationMemberRoleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganizationMemberRoleFormService);
  });

  describe('Service methods', () => {
    describe('createOrganizationMemberRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrganizationMemberRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            joinedAt: expect.any(Object),
            organizationMembership: expect.any(Object),
            organizationRole: expect.any(Object),
          }),
        );
      });

      it('passing IOrganizationMemberRole should create a new form with FormGroup', () => {
        const formGroup = service.createOrganizationMemberRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            joinedAt: expect.any(Object),
            organizationMembership: expect.any(Object),
            organizationRole: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrganizationMemberRole', () => {
      it('should return NewOrganizationMemberRole for default OrganizationMemberRole initial value', () => {
        const formGroup = service.createOrganizationMemberRoleFormGroup(sampleWithNewData);

        const organizationMemberRole = service.getOrganizationMemberRole(formGroup) as any;

        expect(organizationMemberRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrganizationMemberRole for empty OrganizationMemberRole initial value', () => {
        const formGroup = service.createOrganizationMemberRoleFormGroup();

        const organizationMemberRole = service.getOrganizationMemberRole(formGroup) as any;

        expect(organizationMemberRole).toMatchObject({});
      });

      it('should return IOrganizationMemberRole', () => {
        const formGroup = service.createOrganizationMemberRoleFormGroup(sampleWithRequiredData);

        const organizationMemberRole = service.getOrganizationMemberRole(formGroup) as any;

        expect(organizationMemberRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrganizationMemberRole should not enable id FormControl', () => {
        const formGroup = service.createOrganizationMemberRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrganizationMemberRole should disable id FormControl', () => {
        const formGroup = service.createOrganizationMemberRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
