import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAssetCollection, NewAssetCollection } from '../asset-collection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssetCollection for edit and NewAssetCollectionFormGroupInput for create.
 */
type AssetCollectionFormGroupInput = IAssetCollection | PartialWithRequiredKeyOf<NewAssetCollection>;

type AssetCollectionFormDefaults = Pick<NewAssetCollection, 'id' | 'assets'>;

type AssetCollectionFormGroupContent = {
  id: FormControl<IAssetCollection['id'] | NewAssetCollection['id']>;
  name: FormControl<IAssetCollection['name']>;
  fullFilenamePath: FormControl<IAssetCollection['fullFilenamePath']>;
  activationStatus: FormControl<IAssetCollection['activationStatus']>;
  assets: FormControl<IAssetCollection['assets']>;
};

export type AssetCollectionFormGroup = FormGroup<AssetCollectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssetCollectionFormService {
  createAssetCollectionFormGroup(assetCollection: AssetCollectionFormGroupInput = { id: null }): AssetCollectionFormGroup {
    const assetCollectionRawValue = {
      ...this.getFormDefaults(),
      ...assetCollection,
    };
    return new FormGroup<AssetCollectionFormGroupContent>({
      id: new FormControl(
        { value: assetCollectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(assetCollectionRawValue.name, {
        validators: [Validators.required, Validators.maxLength(512)],
      }),
      fullFilenamePath: new FormControl(assetCollectionRawValue.fullFilenamePath, {
        validators: [Validators.maxLength(512)],
      }),
      activationStatus: new FormControl(assetCollectionRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      assets: new FormControl(assetCollectionRawValue.assets ?? []),
    });
  }

  getAssetCollection(form: AssetCollectionFormGroup): IAssetCollection | NewAssetCollection {
    return form.getRawValue() as IAssetCollection | NewAssetCollection;
  }

  resetForm(form: AssetCollectionFormGroup, assetCollection: AssetCollectionFormGroupInput): void {
    const assetCollectionRawValue = { ...this.getFormDefaults(), ...assetCollection };
    form.reset(
      {
        ...assetCollectionRawValue,
        id: { value: assetCollectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssetCollectionFormDefaults {
    return {
      id: null,
      assets: [],
    };
  }
}
