import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrganizationMemberRole, NewOrganizationMemberRole } from '../organization-member-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganizationMemberRole for edit and NewOrganizationMemberRoleFormGroupInput for create.
 */
type OrganizationMemberRoleFormGroupInput = IOrganizationMemberRole | PartialWithRequiredKeyOf<NewOrganizationMemberRole>;

type OrganizationMemberRoleFormDefaults = Pick<NewOrganizationMemberRole, 'id'>;

type OrganizationMemberRoleFormGroupContent = {
  id: FormControl<IOrganizationMemberRole['id'] | NewOrganizationMemberRole['id']>;
  joinedAt: FormControl<IOrganizationMemberRole['joinedAt']>;
  organizationMembership: FormControl<IOrganizationMemberRole['organizationMembership']>;
  organizationRole: FormControl<IOrganizationMemberRole['organizationRole']>;
};

export type OrganizationMemberRoleFormGroup = FormGroup<OrganizationMemberRoleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationMemberRoleFormService {
  createOrganizationMemberRoleFormGroup(
    organizationMemberRole: OrganizationMemberRoleFormGroupInput = { id: null },
  ): OrganizationMemberRoleFormGroup {
    const organizationMemberRoleRawValue = {
      ...this.getFormDefaults(),
      ...organizationMemberRole,
    };
    return new FormGroup<OrganizationMemberRoleFormGroupContent>({
      id: new FormControl(
        { value: organizationMemberRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      joinedAt: new FormControl(organizationMemberRoleRawValue.joinedAt, {
        validators: [Validators.required],
      }),
      organizationMembership: new FormControl(organizationMemberRoleRawValue.organizationMembership, {
        validators: [Validators.required],
      }),
      organizationRole: new FormControl(organizationMemberRoleRawValue.organizationRole, {
        validators: [Validators.required],
      }),
    });
  }

  getOrganizationMemberRole(form: OrganizationMemberRoleFormGroup): IOrganizationMemberRole | NewOrganizationMemberRole {
    return form.getRawValue() as IOrganizationMemberRole | NewOrganizationMemberRole;
  }

  resetForm(form: OrganizationMemberRoleFormGroup, organizationMemberRole: OrganizationMemberRoleFormGroupInput): void {
    const organizationMemberRoleRawValue = { ...this.getFormDefaults(), ...organizationMemberRole };
    form.reset(
      {
        ...organizationMemberRoleRawValue,
        id: { value: organizationMemberRoleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganizationMemberRoleFormDefaults {
    return {
      id: null,
    };
  }
}
