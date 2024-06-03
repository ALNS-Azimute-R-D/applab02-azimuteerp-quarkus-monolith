import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAssetType, NewAssetType } from '../asset-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssetType for edit and NewAssetTypeFormGroupInput for create.
 */
type AssetTypeFormGroupInput = IAssetType | PartialWithRequiredKeyOf<NewAssetType>;

type AssetTypeFormDefaults = Pick<NewAssetType, 'id'>;

type AssetTypeFormGroupContent = {
  id: FormControl<IAssetType['id'] | NewAssetType['id']>;
  acronym: FormControl<IAssetType['acronym']>;
  name: FormControl<IAssetType['name']>;
  description: FormControl<IAssetType['description']>;
  handlerClazzName: FormControl<IAssetType['handlerClazzName']>;
  extraDetails: FormControl<IAssetType['extraDetails']>;
};

export type AssetTypeFormGroup = FormGroup<AssetTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssetTypeFormService {
  createAssetTypeFormGroup(assetType: AssetTypeFormGroupInput = { id: null }): AssetTypeFormGroup {
    const assetTypeRawValue = {
      ...this.getFormDefaults(),
      ...assetType,
    };
    return new FormGroup<AssetTypeFormGroupContent>({
      id: new FormControl(
        { value: assetTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(assetTypeRawValue.acronym, {
        validators: [Validators.maxLength(30)],
      }),
      name: new FormControl(assetTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(assetTypeRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      handlerClazzName: new FormControl(assetTypeRawValue.handlerClazzName, {
        validators: [Validators.maxLength(255)],
      }),
      extraDetails: new FormControl(assetTypeRawValue.extraDetails),
    });
  }

  getAssetType(form: AssetTypeFormGroup): IAssetType | NewAssetType {
    return form.getRawValue() as IAssetType | NewAssetType;
  }

  resetForm(form: AssetTypeFormGroup, assetType: AssetTypeFormGroupInput): void {
    const assetTypeRawValue = { ...this.getFormDefaults(), ...assetType };
    form.reset(
      {
        ...assetTypeRawValue,
        id: { value: assetTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssetTypeFormDefaults {
    return {
      id: null,
    };
  }
}
