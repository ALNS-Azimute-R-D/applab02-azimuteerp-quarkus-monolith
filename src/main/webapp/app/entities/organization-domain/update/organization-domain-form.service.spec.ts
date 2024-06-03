import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../organization-domain.test-samples';

import { OrganizationDomainFormService } from './organization-domain-form.service';

describe('OrganizationDomain Form Service', () => {
  let service: OrganizationDomainFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganizationDomainFormService);
  });

  describe('Service methods', () => {
    describe('createOrganizationDomainFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrganizationDomainFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            domainAcronym: expect.any(Object),
            name: expect.any(Object),
            isVerified: expect.any(Object),
            businessHandlerClazz: expect.any(Object),
            activationStatus: expect.any(Object),
            organization: expect.any(Object),
          }),
        );
      });

      it('passing IOrganizationDomain should create a new form with FormGroup', () => {
        const formGroup = service.createOrganizationDomainFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            domainAcronym: expect.any(Object),
            name: expect.any(Object),
            isVerified: expect.any(Object),
            businessHandlerClazz: expect.any(Object),
            activationStatus: expect.any(Object),
            organization: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrganizationDomain', () => {
      it('should return NewOrganizationDomain for default OrganizationDomain initial value', () => {
        const formGroup = service.createOrganizationDomainFormGroup(sampleWithNewData);

        const organizationDomain = service.getOrganizationDomain(formGroup) as any;

        expect(organizationDomain).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrganizationDomain for empty OrganizationDomain initial value', () => {
        const formGroup = service.createOrganizationDomainFormGroup();

        const organizationDomain = service.getOrganizationDomain(formGroup) as any;

        expect(organizationDomain).toMatchObject({});
      });

      it('should return IOrganizationDomain', () => {
        const formGroup = service.createOrganizationDomainFormGroup(sampleWithRequiredData);

        const organizationDomain = service.getOrganizationDomain(formGroup) as any;

        expect(organizationDomain).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrganizationDomain should not enable id FormControl', () => {
        const formGroup = service.createOrganizationDomainFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrganizationDomain should disable id FormControl', () => {
        const formGroup = service.createOrganizationDomainFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
