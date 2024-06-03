import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../organization-membership.test-samples';

import { OrganizationMembershipFormService } from './organization-membership-form.service';

describe('OrganizationMembership Form Service', () => {
  let service: OrganizationMembershipFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganizationMembershipFormService);
  });

  describe('Service methods', () => {
    describe('createOrganizationMembershipFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrganizationMembershipFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            joinedAt: expect.any(Object),
            activationStatus: expect.any(Object),
            organization: expect.any(Object),
            person: expect.any(Object),
          }),
        );
      });

      it('passing IOrganizationMembership should create a new form with FormGroup', () => {
        const formGroup = service.createOrganizationMembershipFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            joinedAt: expect.any(Object),
            activationStatus: expect.any(Object),
            organization: expect.any(Object),
            person: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrganizationMembership', () => {
      it('should return NewOrganizationMembership for default OrganizationMembership initial value', () => {
        const formGroup = service.createOrganizationMembershipFormGroup(sampleWithNewData);

        const organizationMembership = service.getOrganizationMembership(formGroup) as any;

        expect(organizationMembership).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrganizationMembership for empty OrganizationMembership initial value', () => {
        const formGroup = service.createOrganizationMembershipFormGroup();

        const organizationMembership = service.getOrganizationMembership(formGroup) as any;

        expect(organizationMembership).toMatchObject({});
      });

      it('should return IOrganizationMembership', () => {
        const formGroup = service.createOrganizationMembershipFormGroup(sampleWithRequiredData);

        const organizationMembership = service.getOrganizationMembership(formGroup) as any;

        expect(organizationMembership).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrganizationMembership should not enable id FormControl', () => {
        const formGroup = service.createOrganizationMembershipFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrganizationMembership should disable id FormControl', () => {
        const formGroup = service.createOrganizationMembershipFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
