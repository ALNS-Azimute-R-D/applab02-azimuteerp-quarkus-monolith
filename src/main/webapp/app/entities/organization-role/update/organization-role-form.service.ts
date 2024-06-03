import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrganizationRole, NewOrganizationRole } from '../organization-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganizationRole for edit and NewOrganizationRoleFormGroupInput for create.
 */
type OrganizationRoleFormGroupInput = IOrganizationRole | PartialWithRequiredKeyOf<NewOrganizationRole>;

type OrganizationRoleFormDefaults = Pick<NewOrganizationRole, 'id'>;

type OrganizationRoleFormGroupContent = {
  id: FormControl<IOrganizationRole['id'] | NewOrganizationRole['id']>;
  roleName: FormControl<IOrganizationRole['roleName']>;
  activationStatus: FormControl<IOrganizationRole['activationStatus']>;
  organization: FormControl<IOrganizationRole['organization']>;
};

export type OrganizationRoleFormGroup = FormGroup<OrganizationRoleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationRoleFormService {
  createOrganizationRoleFormGroup(organizationRole: OrganizationRoleFormGroupInput = { id: null }): OrganizationRoleFormGroup {
    const organizationRoleRawValue = {
      ...this.getFormDefaults(),
      ...organizationRole,
    };
    return new FormGroup<OrganizationRoleFormGroupContent>({
      id: new FormControl(
        { value: organizationRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      roleName: new FormControl(organizationRoleRawValue.roleName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      activationStatus: new FormControl(organizationRoleRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      organization: new FormControl(organizationRoleRawValue.organization, {
        validators: [Validators.required],
      }),
    });
  }

  getOrganizationRole(form: OrganizationRoleFormGroup): IOrganizationRole | NewOrganizationRole {
    return form.getRawValue() as IOrganizationRole | NewOrganizationRole;
  }

  resetForm(form: OrganizationRoleFormGroup, organizationRole: OrganizationRoleFormGroupInput): void {
    const organizationRoleRawValue = { ...this.getFormDefaults(), ...organizationRole };
    form.reset(
      {
        ...organizationRoleRawValue,
        id: { value: organizationRoleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganizationRoleFormDefaults {
    return {
      id: null,
    };
  }
}
