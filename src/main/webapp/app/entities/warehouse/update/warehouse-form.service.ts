import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWarehouse, NewWarehouse } from '../warehouse.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWarehouse for edit and NewWarehouseFormGroupInput for create.
 */
type WarehouseFormGroupInput = IWarehouse | PartialWithRequiredKeyOf<NewWarehouse>;

type WarehouseFormDefaults = Pick<NewWarehouse, 'id'>;

type WarehouseFormGroupContent = {
  id: FormControl<IWarehouse['id'] | NewWarehouse['id']>;
  acronym: FormControl<IWarehouse['acronym']>;
  name: FormControl<IWarehouse['name']>;
  description: FormControl<IWarehouse['description']>;
  streetAddress: FormControl<IWarehouse['streetAddress']>;
  houseNumber: FormControl<IWarehouse['houseNumber']>;
  locationName: FormControl<IWarehouse['locationName']>;
  postalCode: FormControl<IWarehouse['postalCode']>;
  pointLocation: FormControl<IWarehouse['pointLocation']>;
  pointLocationContentType: FormControl<IWarehouse['pointLocationContentType']>;
  mainEmail: FormControl<IWarehouse['mainEmail']>;
  landPhoneNumber: FormControl<IWarehouse['landPhoneNumber']>;
  mobilePhoneNumber: FormControl<IWarehouse['mobilePhoneNumber']>;
  faxNumber: FormControl<IWarehouse['faxNumber']>;
  customAttributesDetailsJSON: FormControl<IWarehouse['customAttributesDetailsJSON']>;
  activationStatus: FormControl<IWarehouse['activationStatus']>;
};

export type WarehouseFormGroup = FormGroup<WarehouseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WarehouseFormService {
  createWarehouseFormGroup(warehouse: WarehouseFormGroupInput = { id: null }): WarehouseFormGroup {
    const warehouseRawValue = {
      ...this.getFormDefaults(),
      ...warehouse,
    };
    return new FormGroup<WarehouseFormGroupContent>({
      id: new FormControl(
        { value: warehouseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(warehouseRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      name: new FormControl(warehouseRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(120)],
      }),
      description: new FormControl(warehouseRawValue.description, {
        validators: [Validators.maxLength(1024)],
      }),
      streetAddress: new FormControl(warehouseRawValue.streetAddress, {
        validators: [Validators.required, Validators.maxLength(120)],
      }),
      houseNumber: new FormControl(warehouseRawValue.houseNumber, {
        validators: [Validators.maxLength(20)],
      }),
      locationName: new FormControl(warehouseRawValue.locationName, {
        validators: [Validators.maxLength(50)],
      }),
      postalCode: new FormControl(warehouseRawValue.postalCode, {
        validators: [Validators.required, Validators.maxLength(9)],
      }),
      pointLocation: new FormControl(warehouseRawValue.pointLocation),
      pointLocationContentType: new FormControl(warehouseRawValue.pointLocationContentType),
      mainEmail: new FormControl(warehouseRawValue.mainEmail, {
        validators: [Validators.maxLength(120), Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      landPhoneNumber: new FormControl(warehouseRawValue.landPhoneNumber, {
        validators: [Validators.maxLength(15)],
      }),
      mobilePhoneNumber: new FormControl(warehouseRawValue.mobilePhoneNumber, {
        validators: [Validators.maxLength(15)],
      }),
      faxNumber: new FormControl(warehouseRawValue.faxNumber, {
        validators: [Validators.maxLength(15)],
      }),
      customAttributesDetailsJSON: new FormControl(warehouseRawValue.customAttributesDetailsJSON, {
        validators: [Validators.maxLength(2048)],
      }),
      activationStatus: new FormControl(warehouseRawValue.activationStatus, {
        validators: [Validators.required],
      }),
    });
  }

  getWarehouse(form: WarehouseFormGroup): IWarehouse | NewWarehouse {
    return form.getRawValue() as IWarehouse | NewWarehouse;
  }

  resetForm(form: WarehouseFormGroup, warehouse: WarehouseFormGroupInput): void {
    const warehouseRawValue = { ...this.getFormDefaults(), ...warehouse };
    form.reset(
      {
        ...warehouseRawValue,
        id: { value: warehouseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WarehouseFormDefaults {
    return {
      id: null,
    };
  }
}
