import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAsset, NewAsset } from '../asset.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAsset for edit and NewAssetFormGroupInput for create.
 */
type AssetFormGroupInput = IAsset | PartialWithRequiredKeyOf<NewAsset>;

type AssetFormDefaults = Pick<NewAsset, 'id'>;

type AssetFormGroupContent = {
  id: FormControl<IAsset['id'] | NewAsset['id']>;
  name: FormControl<IAsset['name']>;
  storageTypeUsed: FormControl<IAsset['storageTypeUsed']>;
  fullFilenamePath: FormControl<IAsset['fullFilenamePath']>;
  status: FormControl<IAsset['status']>;
  preferredPurpose: FormControl<IAsset['preferredPurpose']>;
  assetContentAsBlob: FormControl<IAsset['assetContentAsBlob']>;
  assetContentAsBlobContentType: FormControl<IAsset['assetContentAsBlobContentType']>;
  activationStatus: FormControl<IAsset['activationStatus']>;
  assetType: FormControl<IAsset['assetType']>;
  rawAssetProcTmp: FormControl<IAsset['rawAssetProcTmp']>;
};

export type AssetFormGroup = FormGroup<AssetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssetFormService {
  createAssetFormGroup(asset: AssetFormGroupInput = { id: null }): AssetFormGroup {
    const assetRawValue = {
      ...this.getFormDefaults(),
      ...asset,
    };
    return new FormGroup<AssetFormGroupContent>({
      id: new FormControl(
        { value: assetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(assetRawValue.name, {
        validators: [Validators.required, Validators.maxLength(512)],
      }),
      storageTypeUsed: new FormControl(assetRawValue.storageTypeUsed),
      fullFilenamePath: new FormControl(assetRawValue.fullFilenamePath, {
        validators: [Validators.maxLength(512)],
      }),
      status: new FormControl(assetRawValue.status),
      preferredPurpose: new FormControl(assetRawValue.preferredPurpose),
      assetContentAsBlob: new FormControl(assetRawValue.assetContentAsBlob),
      assetContentAsBlobContentType: new FormControl(assetRawValue.assetContentAsBlobContentType),
      activationStatus: new FormControl(assetRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      assetType: new FormControl(assetRawValue.assetType, {
        validators: [Validators.required],
      }),
      rawAssetProcTmp: new FormControl(assetRawValue.rawAssetProcTmp),
    });
  }

  getAsset(form: AssetFormGroup): IAsset | NewAsset {
    return form.getRawValue() as IAsset | NewAsset;
  }

  resetForm(form: AssetFormGroup, asset: AssetFormGroupInput): void {
    const assetRawValue = { ...this.getFormDefaults(), ...asset };
    form.reset(
      {
        ...assetRawValue,
        id: { value: assetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssetFormDefaults {
    return {
      id: null,
    };
  }
}
