import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICommonLocality, NewCommonLocality } from '../common-locality.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommonLocality for edit and NewCommonLocalityFormGroupInput for create.
 */
type CommonLocalityFormGroupInput = ICommonLocality | PartialWithRequiredKeyOf<NewCommonLocality>;

type CommonLocalityFormDefaults = Pick<NewCommonLocality, 'id'>;

type CommonLocalityFormGroupContent = {
  id: FormControl<ICommonLocality['id'] | NewCommonLocality['id']>;
  acronym: FormControl<ICommonLocality['acronym']>;
  name: FormControl<ICommonLocality['name']>;
  description: FormControl<ICommonLocality['description']>;
  streetAddress: FormControl<ICommonLocality['streetAddress']>;
  houseNumber: FormControl<ICommonLocality['houseNumber']>;
  locationName: FormControl<ICommonLocality['locationName']>;
  postalCode: FormControl<ICommonLocality['postalCode']>;
  geoPolygonArea: FormControl<ICommonLocality['geoPolygonArea']>;
  geoPolygonAreaContentType: FormControl<ICommonLocality['geoPolygonAreaContentType']>;
  district: FormControl<ICommonLocality['district']>;
};

export type CommonLocalityFormGroup = FormGroup<CommonLocalityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommonLocalityFormService {
  createCommonLocalityFormGroup(commonLocality: CommonLocalityFormGroupInput = { id: null }): CommonLocalityFormGroup {
    const commonLocalityRawValue = {
      ...this.getFormDefaults(),
      ...commonLocality,
    };
    return new FormGroup<CommonLocalityFormGroupContent>({
      id: new FormControl(
        { value: commonLocalityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(commonLocalityRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      name: new FormControl(commonLocalityRawValue.name, {
        validators: [Validators.required, Validators.maxLength(840)],
      }),
      description: new FormControl(commonLocalityRawValue.description, {
        validators: [Validators.maxLength(512)],
      }),
      streetAddress: new FormControl(commonLocalityRawValue.streetAddress, {
        validators: [Validators.required, Validators.maxLength(120)],
      }),
      houseNumber: new FormControl(commonLocalityRawValue.houseNumber, {
        validators: [Validators.maxLength(20)],
      }),
      locationName: new FormControl(commonLocalityRawValue.locationName, {
        validators: [Validators.maxLength(50)],
      }),
      postalCode: new FormControl(commonLocalityRawValue.postalCode, {
        validators: [Validators.required, Validators.maxLength(9)],
      }),
      geoPolygonArea: new FormControl(commonLocalityRawValue.geoPolygonArea),
      geoPolygonAreaContentType: new FormControl(commonLocalityRawValue.geoPolygonAreaContentType),
      district: new FormControl(commonLocalityRawValue.district, {
        validators: [Validators.required],
      }),
    });
  }

  getCommonLocality(form: CommonLocalityFormGroup): ICommonLocality | NewCommonLocality {
    return form.getRawValue() as ICommonLocality | NewCommonLocality;
  }

  resetForm(form: CommonLocalityFormGroup, commonLocality: CommonLocalityFormGroupInput): void {
    const commonLocalityRawValue = { ...this.getFormDefaults(), ...commonLocality };
    form.reset(
      {
        ...commonLocalityRawValue,
        id: { value: commonLocalityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CommonLocalityFormDefaults {
    return {
      id: null,
    };
  }
}
