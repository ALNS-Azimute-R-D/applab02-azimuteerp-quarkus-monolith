import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRawAssetProcTmp, NewRawAssetProcTmp } from '../raw-asset-proc-tmp.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRawAssetProcTmp for edit and NewRawAssetProcTmpFormGroupInput for create.
 */
type RawAssetProcTmpFormGroupInput = IRawAssetProcTmp | PartialWithRequiredKeyOf<NewRawAssetProcTmp>;

type RawAssetProcTmpFormDefaults = Pick<NewRawAssetProcTmp, 'id'>;

type RawAssetProcTmpFormGroupContent = {
  id: FormControl<IRawAssetProcTmp['id'] | NewRawAssetProcTmp['id']>;
  name: FormControl<IRawAssetProcTmp['name']>;
  statusRawProcessing: FormControl<IRawAssetProcTmp['statusRawProcessing']>;
  fullFilenamePath: FormControl<IRawAssetProcTmp['fullFilenamePath']>;
  assetRawContentAsBlob: FormControl<IRawAssetProcTmp['assetRawContentAsBlob']>;
  assetRawContentAsBlobContentType: FormControl<IRawAssetProcTmp['assetRawContentAsBlobContentType']>;
  customAttributesDetailsJSON: FormControl<IRawAssetProcTmp['customAttributesDetailsJSON']>;
  assetType: FormControl<IRawAssetProcTmp['assetType']>;
};

export type RawAssetProcTmpFormGroup = FormGroup<RawAssetProcTmpFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RawAssetProcTmpFormService {
  createRawAssetProcTmpFormGroup(rawAssetProcTmp: RawAssetProcTmpFormGroupInput = { id: null }): RawAssetProcTmpFormGroup {
    const rawAssetProcTmpRawValue = {
      ...this.getFormDefaults(),
      ...rawAssetProcTmp,
    };
    return new FormGroup<RawAssetProcTmpFormGroupContent>({
      id: new FormControl(
        { value: rawAssetProcTmpRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(rawAssetProcTmpRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      statusRawProcessing: new FormControl(rawAssetProcTmpRawValue.statusRawProcessing),
      fullFilenamePath: new FormControl(rawAssetProcTmpRawValue.fullFilenamePath, {
        validators: [Validators.maxLength(512)],
      }),
      assetRawContentAsBlob: new FormControl(rawAssetProcTmpRawValue.assetRawContentAsBlob),
      assetRawContentAsBlobContentType: new FormControl(rawAssetProcTmpRawValue.assetRawContentAsBlobContentType),
      customAttributesDetailsJSON: new FormControl(rawAssetProcTmpRawValue.customAttributesDetailsJSON, {
        validators: [Validators.maxLength(4096)],
      }),
      assetType: new FormControl(rawAssetProcTmpRawValue.assetType, {
        validators: [Validators.required],
      }),
    });
  }

  getRawAssetProcTmp(form: RawAssetProcTmpFormGroup): IRawAssetProcTmp | NewRawAssetProcTmp {
    return form.getRawValue() as IRawAssetProcTmp | NewRawAssetProcTmp;
  }

  resetForm(form: RawAssetProcTmpFormGroup, rawAssetProcTmp: RawAssetProcTmpFormGroupInput): void {
    const rawAssetProcTmpRawValue = { ...this.getFormDefaults(), ...rawAssetProcTmp };
    form.reset(
      {
        ...rawAssetProcTmpRawValue,
        id: { value: rawAssetProcTmpRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RawAssetProcTmpFormDefaults {
    return {
      id: null,
    };
  }
}
