import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrganization, NewOrganization } from '../organization.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganization for edit and NewOrganizationFormGroupInput for create.
 */
type OrganizationFormGroupInput = IOrganization | PartialWithRequiredKeyOf<NewOrganization>;

type OrganizationFormDefaults = Pick<NewOrganization, 'id'>;

type OrganizationFormGroupContent = {
  id: FormControl<IOrganization['id'] | NewOrganization['id']>;
  acronym: FormControl<IOrganization['acronym']>;
  businessCode: FormControl<IOrganization['businessCode']>;
  hierarchicalLevel: FormControl<IOrganization['hierarchicalLevel']>;
  name: FormControl<IOrganization['name']>;
  description: FormControl<IOrganization['description']>;
  businessHandlerClazz: FormControl<IOrganization['businessHandlerClazz']>;
  mainContactPersonDetailsJSON: FormControl<IOrganization['mainContactPersonDetailsJSON']>;
  technicalEnvironmentsDetailsJSON: FormControl<IOrganization['technicalEnvironmentsDetailsJSON']>;
  customAttributesDetailsJSON: FormControl<IOrganization['customAttributesDetailsJSON']>;
  organizationStatus: FormControl<IOrganization['organizationStatus']>;
  activationStatus: FormControl<IOrganization['activationStatus']>;
  logoImg: FormControl<IOrganization['logoImg']>;
  logoImgContentType: FormControl<IOrganization['logoImgContentType']>;
  tenant: FormControl<IOrganization['tenant']>;
  typeOfOrganization: FormControl<IOrganization['typeOfOrganization']>;
  organizationParent: FormControl<IOrganization['organizationParent']>;
};

export type OrganizationFormGroup = FormGroup<OrganizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationFormService {
  createOrganizationFormGroup(organization: OrganizationFormGroupInput = { id: null }): OrganizationFormGroup {
    const organizationRawValue = {
      ...this.getFormDefaults(),
      ...organization,
    };
    return new FormGroup<OrganizationFormGroupContent>({
      id: new FormControl(
        { value: organizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(organizationRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      businessCode: new FormControl(organizationRawValue.businessCode, {
        validators: [Validators.required, Validators.maxLength(15)],
      }),
      hierarchicalLevel: new FormControl(organizationRawValue.hierarchicalLevel, {
        validators: [Validators.maxLength(30)],
      }),
      name: new FormControl(organizationRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(255)],
      }),
      description: new FormControl(organizationRawValue.description, {
        validators: [Validators.required, Validators.maxLength(512)],
      }),
      businessHandlerClazz: new FormControl(organizationRawValue.businessHandlerClazz, {
        validators: [Validators.maxLength(512)],
      }),
      mainContactPersonDetailsJSON: new FormControl(organizationRawValue.mainContactPersonDetailsJSON, {
        validators: [Validators.maxLength(2048)],
      }),
      technicalEnvironmentsDetailsJSON: new FormControl(organizationRawValue.technicalEnvironmentsDetailsJSON, {
        validators: [Validators.maxLength(4096)],
      }),
      customAttributesDetailsJSON: new FormControl(organizationRawValue.customAttributesDetailsJSON, {
        validators: [Validators.maxLength(4096)],
      }),
      organizationStatus: new FormControl(organizationRawValue.organizationStatus, {
        validators: [Validators.required],
      }),
      activationStatus: new FormControl(organizationRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      logoImg: new FormControl(organizationRawValue.logoImg),
      logoImgContentType: new FormControl(organizationRawValue.logoImgContentType),
      tenant: new FormControl(organizationRawValue.tenant, {
        validators: [Validators.required],
      }),
      typeOfOrganization: new FormControl(organizationRawValue.typeOfOrganization, {
        validators: [Validators.required],
      }),
      organizationParent: new FormControl(organizationRawValue.organizationParent),
    });
  }

  getOrganization(form: OrganizationFormGroup): IOrganization | NewOrganization {
    return form.getRawValue() as IOrganization | NewOrganization;
  }

  resetForm(form: OrganizationFormGroup, organization: OrganizationFormGroupInput): void {
    const organizationRawValue = { ...this.getFormDefaults(), ...organization };
    form.reset(
      {
        ...organizationRawValue,
        id: { value: organizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganizationFormDefaults {
    return {
      id: null,
    };
  }
}
