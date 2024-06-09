import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrganizationAttribute, NewOrganizationAttribute } from '../organization-attribute.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganizationAttribute for edit and NewOrganizationAttributeFormGroupInput for create.
 */
type OrganizationAttributeFormGroupInput = IOrganizationAttribute | PartialWithRequiredKeyOf<NewOrganizationAttribute>;

type OrganizationAttributeFormDefaults = Pick<NewOrganizationAttribute, 'id'>;

type OrganizationAttributeFormGroupContent = {
  id: FormControl<IOrganizationAttribute['id'] | NewOrganizationAttribute['id']>;
  attributeName: FormControl<IOrganizationAttribute['attributeName']>;
  attributeValue: FormControl<IOrganizationAttribute['attributeValue']>;
  organization: FormControl<IOrganizationAttribute['organization']>;
};

export type OrganizationAttributeFormGroup = FormGroup<OrganizationAttributeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationAttributeFormService {
  createOrganizationAttributeFormGroup(
    organizationAttribute: OrganizationAttributeFormGroupInput = { id: null },
  ): OrganizationAttributeFormGroup {
    const organizationAttributeRawValue = {
      ...this.getFormDefaults(),
      ...organizationAttribute,
    };
    return new FormGroup<OrganizationAttributeFormGroupContent>({
      id: new FormControl(
        { value: organizationAttributeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      attributeName: new FormControl(organizationAttributeRawValue.attributeName, {
        validators: [Validators.maxLength(255)],
      }),
      attributeValue: new FormControl(organizationAttributeRawValue.attributeValue, {
        validators: [Validators.maxLength(4096)],
      }),
      organization: new FormControl(organizationAttributeRawValue.organization, {
        validators: [Validators.required],
      }),
    });
  }

  getOrganizationAttribute(form: OrganizationAttributeFormGroup): IOrganizationAttribute | NewOrganizationAttribute {
    return form.getRawValue() as IOrganizationAttribute | NewOrganizationAttribute;
  }

  resetForm(form: OrganizationAttributeFormGroup, organizationAttribute: OrganizationAttributeFormGroupInput): void {
    const organizationAttributeRawValue = { ...this.getFormDefaults(), ...organizationAttribute };
    form.reset(
      {
        ...organizationAttributeRawValue,
        id: { value: organizationAttributeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganizationAttributeFormDefaults {
    return {
      id: null,
    };
  }
}
