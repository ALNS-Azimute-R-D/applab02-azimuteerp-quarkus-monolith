import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBusinessUnit, NewBusinessUnit } from '../business-unit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBusinessUnit for edit and NewBusinessUnitFormGroupInput for create.
 */
type BusinessUnitFormGroupInput = IBusinessUnit | PartialWithRequiredKeyOf<NewBusinessUnit>;

type BusinessUnitFormDefaults = Pick<NewBusinessUnit, 'id'>;

type BusinessUnitFormGroupContent = {
  id: FormControl<IBusinessUnit['id'] | NewBusinessUnit['id']>;
  acronym: FormControl<IBusinessUnit['acronym']>;
  hierarchicalLevel: FormControl<IBusinessUnit['hierarchicalLevel']>;
  name: FormControl<IBusinessUnit['name']>;
  activationStatus: FormControl<IBusinessUnit['activationStatus']>;
  organization: FormControl<IBusinessUnit['organization']>;
  businessUnitParent: FormControl<IBusinessUnit['businessUnitParent']>;
};

export type BusinessUnitFormGroup = FormGroup<BusinessUnitFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BusinessUnitFormService {
  createBusinessUnitFormGroup(businessUnit: BusinessUnitFormGroupInput = { id: null }): BusinessUnitFormGroup {
    const businessUnitRawValue = {
      ...this.getFormDefaults(),
      ...businessUnit,
    };
    return new FormGroup<BusinessUnitFormGroupContent>({
      id: new FormControl(
        { value: businessUnitRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(businessUnitRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      hierarchicalLevel: new FormControl(businessUnitRawValue.hierarchicalLevel, {
        validators: [Validators.maxLength(30)],
      }),
      name: new FormControl(businessUnitRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(120)],
      }),
      activationStatus: new FormControl(businessUnitRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      organization: new FormControl(businessUnitRawValue.organization, {
        validators: [Validators.required],
      }),
      businessUnitParent: new FormControl(businessUnitRawValue.businessUnitParent),
    });
  }

  getBusinessUnit(form: BusinessUnitFormGroup): IBusinessUnit | NewBusinessUnit {
    return form.getRawValue() as IBusinessUnit | NewBusinessUnit;
  }

  resetForm(form: BusinessUnitFormGroup, businessUnit: BusinessUnitFormGroupInput): void {
    const businessUnitRawValue = { ...this.getFormDefaults(), ...businessUnit };
    form.reset(
      {
        ...businessUnitRawValue,
        id: { value: businessUnitRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BusinessUnitFormDefaults {
    return {
      id: null,
    };
  }
}
