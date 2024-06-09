import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITenant, NewTenant } from '../tenant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITenant for edit and NewTenantFormGroupInput for create.
 */
type TenantFormGroupInput = ITenant | PartialWithRequiredKeyOf<NewTenant>;

type TenantFormDefaults = Pick<NewTenant, 'id'>;

type TenantFormGroupContent = {
  id: FormControl<ITenant['id'] | NewTenant['id']>;
  acronym: FormControl<ITenant['acronym']>;
  name: FormControl<ITenant['name']>;
  description: FormControl<ITenant['description']>;
  customerBusinessCode: FormControl<ITenant['customerBusinessCode']>;
  businessHandlerClazz: FormControl<ITenant['businessHandlerClazz']>;
  mainContactPersonDetailsJSON: FormControl<ITenant['mainContactPersonDetailsJSON']>;
  billingDetailsJSON: FormControl<ITenant['billingDetailsJSON']>;
  technicalEnvironmentsDetailsJSON: FormControl<ITenant['technicalEnvironmentsDetailsJSON']>;
  customAttributesDetailsJSON: FormControl<ITenant['customAttributesDetailsJSON']>;
  logoImg: FormControl<ITenant['logoImg']>;
  logoImgContentType: FormControl<ITenant['logoImgContentType']>;
  activationStatus: FormControl<ITenant['activationStatus']>;
};

export type TenantFormGroup = FormGroup<TenantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TenantFormService {
  createTenantFormGroup(tenant: TenantFormGroupInput = { id: null }): TenantFormGroup {
    const tenantRawValue = {
      ...this.getFormDefaults(),
      ...tenant,
    };
    return new FormGroup<TenantFormGroupContent>({
      id: new FormControl(
        { value: tenantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(tenantRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      name: new FormControl(tenantRawValue.name, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      description: new FormControl(tenantRawValue.description, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      customerBusinessCode: new FormControl(tenantRawValue.customerBusinessCode, {
        validators: [Validators.required, Validators.maxLength(15)],
      }),
      businessHandlerClazz: new FormControl(tenantRawValue.businessHandlerClazz, {
        validators: [Validators.maxLength(512)],
      }),
      mainContactPersonDetailsJSON: new FormControl(tenantRawValue.mainContactPersonDetailsJSON, {
        validators: [Validators.maxLength(2048)],
      }),
      billingDetailsJSON: new FormControl(tenantRawValue.billingDetailsJSON, {
        validators: [Validators.maxLength(2048)],
      }),
      technicalEnvironmentsDetailsJSON: new FormControl(tenantRawValue.technicalEnvironmentsDetailsJSON, {
        validators: [Validators.maxLength(4096)],
      }),
      customAttributesDetailsJSON: new FormControl(tenantRawValue.customAttributesDetailsJSON, {
        validators: [Validators.maxLength(4096)],
      }),
      logoImg: new FormControl(tenantRawValue.logoImg),
      logoImgContentType: new FormControl(tenantRawValue.logoImgContentType),
      activationStatus: new FormControl(tenantRawValue.activationStatus, {
        validators: [Validators.required],
      }),
    });
  }

  getTenant(form: TenantFormGroup): ITenant | NewTenant {
    return form.getRawValue() as ITenant | NewTenant;
  }

  resetForm(form: TenantFormGroup, tenant: TenantFormGroupInput): void {
    const tenantRawValue = { ...this.getFormDefaults(), ...tenant };
    form.reset(
      {
        ...tenantRawValue,
        id: { value: tenantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TenantFormDefaults {
    return {
      id: null,
    };
  }
}
