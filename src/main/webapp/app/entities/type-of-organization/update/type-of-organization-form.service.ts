import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeOfOrganization, NewTypeOfOrganization } from '../type-of-organization.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeOfOrganization for edit and NewTypeOfOrganizationFormGroupInput for create.
 */
type TypeOfOrganizationFormGroupInput = ITypeOfOrganization | PartialWithRequiredKeyOf<NewTypeOfOrganization>;

type TypeOfOrganizationFormDefaults = Pick<NewTypeOfOrganization, 'id'>;

type TypeOfOrganizationFormGroupContent = {
  id: FormControl<ITypeOfOrganization['id'] | NewTypeOfOrganization['id']>;
  acronym: FormControl<ITypeOfOrganization['acronym']>;
  name: FormControl<ITypeOfOrganization['name']>;
  description: FormControl<ITypeOfOrganization['description']>;
  businessHandlerClazz: FormControl<ITypeOfOrganization['businessHandlerClazz']>;
};

export type TypeOfOrganizationFormGroup = FormGroup<TypeOfOrganizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeOfOrganizationFormService {
  createTypeOfOrganizationFormGroup(typeOfOrganization: TypeOfOrganizationFormGroupInput = { id: null }): TypeOfOrganizationFormGroup {
    const typeOfOrganizationRawValue = {
      ...this.getFormDefaults(),
      ...typeOfOrganization,
    };
    return new FormGroup<TypeOfOrganizationFormGroupContent>({
      id: new FormControl(
        { value: typeOfOrganizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(typeOfOrganizationRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      name: new FormControl(typeOfOrganizationRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(typeOfOrganizationRawValue.description, {
        validators: [Validators.required],
      }),
      businessHandlerClazz: new FormControl(typeOfOrganizationRawValue.businessHandlerClazz, {
        validators: [Validators.maxLength(512)],
      }),
    });
  }

  getTypeOfOrganization(form: TypeOfOrganizationFormGroup): ITypeOfOrganization | NewTypeOfOrganization {
    return form.getRawValue() as ITypeOfOrganization | NewTypeOfOrganization;
  }

  resetForm(form: TypeOfOrganizationFormGroup, typeOfOrganization: TypeOfOrganizationFormGroupInput): void {
    const typeOfOrganizationRawValue = { ...this.getFormDefaults(), ...typeOfOrganization };
    form.reset(
      {
        ...typeOfOrganizationRawValue,
        id: { value: typeOfOrganizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeOfOrganizationFormDefaults {
    return {
      id: null,
    };
  }
}
