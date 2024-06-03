import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITownCity, NewTownCity } from '../town-city.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITownCity for edit and NewTownCityFormGroupInput for create.
 */
type TownCityFormGroupInput = ITownCity | PartialWithRequiredKeyOf<NewTownCity>;

type TownCityFormDefaults = Pick<NewTownCity, 'id'>;

type TownCityFormGroupContent = {
  id: FormControl<ITownCity['id'] | NewTownCity['id']>;
  acronym: FormControl<ITownCity['acronym']>;
  name: FormControl<ITownCity['name']>;
  geoPolygonArea: FormControl<ITownCity['geoPolygonArea']>;
  geoPolygonAreaContentType: FormControl<ITownCity['geoPolygonAreaContentType']>;
  province: FormControl<ITownCity['province']>;
};

export type TownCityFormGroup = FormGroup<TownCityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TownCityFormService {
  createTownCityFormGroup(townCity: TownCityFormGroupInput = { id: null }): TownCityFormGroup {
    const townCityRawValue = {
      ...this.getFormDefaults(),
      ...townCity,
    };
    return new FormGroup<TownCityFormGroupContent>({
      id: new FormControl(
        { value: townCityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(townCityRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(8)],
      }),
      name: new FormControl(townCityRawValue.name, {
        validators: [Validators.required, Validators.maxLength(40)],
      }),
      geoPolygonArea: new FormControl(townCityRawValue.geoPolygonArea),
      geoPolygonAreaContentType: new FormControl(townCityRawValue.geoPolygonAreaContentType),
      province: new FormControl(townCityRawValue.province, {
        validators: [Validators.required],
      }),
    });
  }

  getTownCity(form: TownCityFormGroup): ITownCity | NewTownCity {
    return form.getRawValue() as ITownCity | NewTownCity;
  }

  resetForm(form: TownCityFormGroup, townCity: TownCityFormGroupInput): void {
    const townCityRawValue = { ...this.getFormDefaults(), ...townCity };
    form.reset(
      {
        ...townCityRawValue,
        id: { value: townCityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TownCityFormDefaults {
    return {
      id: null,
    };
  }
}
