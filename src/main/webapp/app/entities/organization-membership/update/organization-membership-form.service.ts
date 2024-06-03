import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrganizationMembership, NewOrganizationMembership } from '../organization-membership.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganizationMembership for edit and NewOrganizationMembershipFormGroupInput for create.
 */
type OrganizationMembershipFormGroupInput = IOrganizationMembership | PartialWithRequiredKeyOf<NewOrganizationMembership>;

type OrganizationMembershipFormDefaults = Pick<NewOrganizationMembership, 'id'>;

type OrganizationMembershipFormGroupContent = {
  id: FormControl<IOrganizationMembership['id'] | NewOrganizationMembership['id']>;
  joinedAt: FormControl<IOrganizationMembership['joinedAt']>;
  activationStatus: FormControl<IOrganizationMembership['activationStatus']>;
  organization: FormControl<IOrganizationMembership['organization']>;
  person: FormControl<IOrganizationMembership['person']>;
};

export type OrganizationMembershipFormGroup = FormGroup<OrganizationMembershipFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationMembershipFormService {
  createOrganizationMembershipFormGroup(
    organizationMembership: OrganizationMembershipFormGroupInput = { id: null },
  ): OrganizationMembershipFormGroup {
    const organizationMembershipRawValue = {
      ...this.getFormDefaults(),
      ...organizationMembership,
    };
    return new FormGroup<OrganizationMembershipFormGroupContent>({
      id: new FormControl(
        { value: organizationMembershipRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      joinedAt: new FormControl(organizationMembershipRawValue.joinedAt, {
        validators: [Validators.required],
      }),
      activationStatus: new FormControl(organizationMembershipRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      organization: new FormControl(organizationMembershipRawValue.organization, {
        validators: [Validators.required],
      }),
      person: new FormControl(organizationMembershipRawValue.person, {
        validators: [Validators.required],
      }),
    });
  }

  getOrganizationMembership(form: OrganizationMembershipFormGroup): IOrganizationMembership | NewOrganizationMembership {
    return form.getRawValue() as IOrganizationMembership | NewOrganizationMembership;
  }

  resetForm(form: OrganizationMembershipFormGroup, organizationMembership: OrganizationMembershipFormGroupInput): void {
    const organizationMembershipRawValue = { ...this.getFormDefaults(), ...organizationMembership };
    form.reset(
      {
        ...organizationMembershipRawValue,
        id: { value: organizationMembershipRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganizationMembershipFormDefaults {
    return {
      id: null,
    };
  }
}
