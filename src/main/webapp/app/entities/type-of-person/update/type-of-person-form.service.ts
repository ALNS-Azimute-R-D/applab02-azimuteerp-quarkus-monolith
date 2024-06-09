import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeOfPerson, NewTypeOfPerson } from '../type-of-person.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeOfPerson for edit and NewTypeOfPersonFormGroupInput for create.
 */
type TypeOfPersonFormGroupInput = ITypeOfPerson | PartialWithRequiredKeyOf<NewTypeOfPerson>;

type TypeOfPersonFormDefaults = Pick<NewTypeOfPerson, 'id'>;

type TypeOfPersonFormGroupContent = {
  id: FormControl<ITypeOfPerson['id'] | NewTypeOfPerson['id']>;
  code: FormControl<ITypeOfPerson['code']>;
  description: FormControl<ITypeOfPerson['description']>;
};

export type TypeOfPersonFormGroup = FormGroup<TypeOfPersonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeOfPersonFormService {
  createTypeOfPersonFormGroup(typeOfPerson: TypeOfPersonFormGroupInput = { id: null }): TypeOfPersonFormGroup {
    const typeOfPersonRawValue = {
      ...this.getFormDefaults(),
      ...typeOfPerson,
    };
    return new FormGroup<TypeOfPersonFormGroupContent>({
      id: new FormControl(
        { value: typeOfPersonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(typeOfPersonRawValue.code, {
        validators: [Validators.required, Validators.maxLength(5)],
      }),
      description: new FormControl(typeOfPersonRawValue.description, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
    });
  }

  getTypeOfPerson(form: TypeOfPersonFormGroup): ITypeOfPerson | NewTypeOfPerson {
    return form.getRawValue() as ITypeOfPerson | NewTypeOfPerson;
  }

  resetForm(form: TypeOfPersonFormGroup, typeOfPerson: TypeOfPersonFormGroupInput): void {
    const typeOfPersonRawValue = { ...this.getFormDefaults(), ...typeOfPerson };
    form.reset(
      {
        ...typeOfPersonRawValue,
        id: { value: typeOfPersonRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeOfPersonFormDefaults {
    return {
      id: null,
    };
  }
}
