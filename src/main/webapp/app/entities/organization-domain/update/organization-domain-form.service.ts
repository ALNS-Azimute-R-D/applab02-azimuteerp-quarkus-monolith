import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrganizationDomain, NewOrganizationDomain } from '../organization-domain.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganizationDomain for edit and NewOrganizationDomainFormGroupInput for create.
 */
type OrganizationDomainFormGroupInput = IOrganizationDomain | PartialWithRequiredKeyOf<NewOrganizationDomain>;

type OrganizationDomainFormDefaults = Pick<NewOrganizationDomain, 'id' | 'isVerified'>;

type OrganizationDomainFormGroupContent = {
  id: FormControl<IOrganizationDomain['id'] | NewOrganizationDomain['id']>;
  domainAcronym: FormControl<IOrganizationDomain['domainAcronym']>;
  name: FormControl<IOrganizationDomain['name']>;
  isVerified: FormControl<IOrganizationDomain['isVerified']>;
  businessHandlerClazz: FormControl<IOrganizationDomain['businessHandlerClazz']>;
  activationStatus: FormControl<IOrganizationDomain['activationStatus']>;
  organization: FormControl<IOrganizationDomain['organization']>;
};

export type OrganizationDomainFormGroup = FormGroup<OrganizationDomainFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationDomainFormService {
  createOrganizationDomainFormGroup(organizationDomain: OrganizationDomainFormGroupInput = { id: null }): OrganizationDomainFormGroup {
    const organizationDomainRawValue = {
      ...this.getFormDefaults(),
      ...organizationDomain,
    };
    return new FormGroup<OrganizationDomainFormGroupContent>({
      id: new FormControl(
        { value: organizationDomainRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      domainAcronym: new FormControl(organizationDomainRawValue.domainAcronym, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      name: new FormControl(organizationDomainRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(255)],
      }),
      isVerified: new FormControl(organizationDomainRawValue.isVerified, {
        validators: [Validators.required],
      }),
      businessHandlerClazz: new FormControl(organizationDomainRawValue.businessHandlerClazz, {
        validators: [Validators.maxLength(512)],
      }),
      activationStatus: new FormControl(organizationDomainRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      organization: new FormControl(organizationDomainRawValue.organization, {
        validators: [Validators.required],
      }),
    });
  }

  getOrganizationDomain(form: OrganizationDomainFormGroup): IOrganizationDomain | NewOrganizationDomain {
    return form.getRawValue() as IOrganizationDomain | NewOrganizationDomain;
  }

  resetForm(form: OrganizationDomainFormGroup, organizationDomain: OrganizationDomainFormGroupInput): void {
    const organizationDomainRawValue = { ...this.getFormDefaults(), ...organizationDomain };
    form.reset(
      {
        ...organizationDomainRawValue,
        id: { value: organizationDomainRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganizationDomainFormDefaults {
    return {
      id: null,
      isVerified: false,
    };
  }
}
