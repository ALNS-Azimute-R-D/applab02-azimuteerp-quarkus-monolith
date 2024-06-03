import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDistrict, NewDistrict } from '../district.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDistrict for edit and NewDistrictFormGroupInput for create.
 */
type DistrictFormGroupInput = IDistrict | PartialWithRequiredKeyOf<NewDistrict>;

type DistrictFormDefaults = Pick<NewDistrict, 'id'>;

type DistrictFormGroupContent = {
  id: FormControl<IDistrict['id'] | NewDistrict['id']>;
  acronym: FormControl<IDistrict['acronym']>;
  name: FormControl<IDistrict['name']>;
  geoPolygonArea: FormControl<IDistrict['geoPolygonArea']>;
  geoPolygonAreaContentType: FormControl<IDistrict['geoPolygonAreaContentType']>;
  townCity: FormControl<IDistrict['townCity']>;
};

export type DistrictFormGroup = FormGroup<DistrictFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DistrictFormService {
  createDistrictFormGroup(district: DistrictFormGroupInput = { id: null }): DistrictFormGroup {
    const districtRawValue = {
      ...this.getFormDefaults(),
      ...district,
    };
    return new FormGroup<DistrictFormGroupContent>({
      id: new FormControl(
        { value: districtRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(districtRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(8)],
      }),
      name: new FormControl(districtRawValue.name, {
        validators: [Validators.required, Validators.maxLength(40)],
      }),
      geoPolygonArea: new FormControl(districtRawValue.geoPolygonArea),
      geoPolygonAreaContentType: new FormControl(districtRawValue.geoPolygonAreaContentType),
      townCity: new FormControl(districtRawValue.townCity, {
        validators: [Validators.required],
      }),
    });
  }

  getDistrict(form: DistrictFormGroup): IDistrict | NewDistrict {
    return form.getRawValue() as IDistrict | NewDistrict;
  }

  resetForm(form: DistrictFormGroup, district: DistrictFormGroupInput): void {
    const districtRawValue = { ...this.getFormDefaults(), ...district };
    form.reset(
      {
        ...districtRawValue,
        id: { value: districtRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DistrictFormDefaults {
    return {
      id: null,
    };
  }
}
