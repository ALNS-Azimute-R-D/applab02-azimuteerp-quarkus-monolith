import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILocality, NewLocality } from '../locality.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocality for edit and NewLocalityFormGroupInput for create.
 */
type LocalityFormGroupInput = ILocality | PartialWithRequiredKeyOf<NewLocality>;

type LocalityFormDefaults = Pick<NewLocality, 'id'>;

type LocalityFormGroupContent = {
  id: FormControl<ILocality['id'] | NewLocality['id']>;
  acronym: FormControl<ILocality['acronym']>;
  name: FormControl<ILocality['name']>;
  description: FormControl<ILocality['description']>;
  geoPolygonArea: FormControl<ILocality['geoPolygonArea']>;
  geoPolygonAreaContentType: FormControl<ILocality['geoPolygonAreaContentType']>;
  country: FormControl<ILocality['country']>;
};

export type LocalityFormGroup = FormGroup<LocalityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocalityFormService {
  createLocalityFormGroup(locality: LocalityFormGroupInput = { id: null }): LocalityFormGroup {
    const localityRawValue = {
      ...this.getFormDefaults(),
      ...locality,
    };
    return new FormGroup<LocalityFormGroupContent>({
      id: new FormControl(
        { value: localityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(localityRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(8)],
      }),
      name: new FormControl(localityRawValue.name, {
        validators: [Validators.required, Validators.maxLength(840)],
      }),
      description: new FormControl(localityRawValue.description),
      geoPolygonArea: new FormControl(localityRawValue.geoPolygonArea),
      geoPolygonAreaContentType: new FormControl(localityRawValue.geoPolygonAreaContentType),
      country: new FormControl(localityRawValue.country, {
        validators: [Validators.required],
      }),
    });
  }

  getLocality(form: LocalityFormGroup): ILocality | NewLocality {
    return form.getRawValue() as ILocality | NewLocality;
  }

  resetForm(form: LocalityFormGroup, locality: LocalityFormGroupInput): void {
    const localityRawValue = { ...this.getFormDefaults(), ...locality };
    form.reset(
      {
        ...localityRawValue,
        id: { value: localityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LocalityFormDefaults {
    return {
      id: null,
    };
  }
}
