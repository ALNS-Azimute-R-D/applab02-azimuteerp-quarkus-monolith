import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategory, NewCategory } from '../category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategory for edit and NewCategoryFormGroupInput for create.
 */
type CategoryFormGroupInput = ICategory | PartialWithRequiredKeyOf<NewCategory>;

type CategoryFormDefaults = Pick<NewCategory, 'id'>;

type CategoryFormGroupContent = {
  id: FormControl<ICategory['id'] | NewCategory['id']>;
  acronym: FormControl<ICategory['acronym']>;
  name: FormControl<ICategory['name']>;
  description: FormControl<ICategory['description']>;
  handlerClazzName: FormControl<ICategory['handlerClazzName']>;
  categoryParent: FormControl<ICategory['categoryParent']>;
};

export type CategoryFormGroup = FormGroup<CategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoryFormService {
  createCategoryFormGroup(category: CategoryFormGroupInput = { id: null }): CategoryFormGroup {
    const categoryRawValue = {
      ...this.getFormDefaults(),
      ...category,
    };
    return new FormGroup<CategoryFormGroupContent>({
      id: new FormControl(
        { value: categoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(categoryRawValue.acronym, {
        validators: [Validators.maxLength(30)],
      }),
      name: new FormControl(categoryRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(categoryRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      handlerClazzName: new FormControl(categoryRawValue.handlerClazzName, {
        validators: [Validators.maxLength(255)],
      }),
      categoryParent: new FormControl(categoryRawValue.categoryParent),
    });
  }

  getCategory(form: CategoryFormGroup): ICategory | NewCategory {
    return form.getRawValue() as ICategory | NewCategory;
  }

  resetForm(form: CategoryFormGroup, category: CategoryFormGroupInput): void {
    const categoryRawValue = { ...this.getFormDefaults(), ...category };
    form.reset(
      {
        ...categoryRawValue,
        id: { value: categoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CategoryFormDefaults {
    return {
      id: null,
    };
  }
}
