import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAssetMetadata, NewAssetMetadata } from '../asset-metadata.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssetMetadata for edit and NewAssetMetadataFormGroupInput for create.
 */
type AssetMetadataFormGroupInput = IAssetMetadata | PartialWithRequiredKeyOf<NewAssetMetadata>;

type AssetMetadataFormDefaults = Pick<NewAssetMetadata, 'id'>;

type AssetMetadataFormGroupContent = {
  id: FormControl<IAssetMetadata['id'] | NewAssetMetadata['id']>;
  metadataDetailsJSON: FormControl<IAssetMetadata['metadataDetailsJSON']>;
  asset: FormControl<IAssetMetadata['asset']>;
};

export type AssetMetadataFormGroup = FormGroup<AssetMetadataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssetMetadataFormService {
  createAssetMetadataFormGroup(assetMetadata: AssetMetadataFormGroupInput = { id: null }): AssetMetadataFormGroup {
    const assetMetadataRawValue = {
      ...this.getFormDefaults(),
      ...assetMetadata,
    };
    return new FormGroup<AssetMetadataFormGroupContent>({
      id: new FormControl(
        { value: assetMetadataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      metadataDetailsJSON: new FormControl(assetMetadataRawValue.metadataDetailsJSON, {
        validators: [Validators.maxLength(8192)],
      }),
      asset: new FormControl(assetMetadataRawValue.asset, {
        validators: [Validators.required],
      }),
    });
  }

  getAssetMetadata(form: AssetMetadataFormGroup): IAssetMetadata | NewAssetMetadata {
    return form.getRawValue() as IAssetMetadata | NewAssetMetadata;
  }

  resetForm(form: AssetMetadataFormGroup, assetMetadata: AssetMetadataFormGroupInput): void {
    const assetMetadataRawValue = { ...this.getFormDefaults(), ...assetMetadata };
    form.reset(
      {
        ...assetMetadataRawValue,
        id: { value: assetMetadataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AssetMetadataFormDefaults {
    return {
      id: null,
    };
  }
}
