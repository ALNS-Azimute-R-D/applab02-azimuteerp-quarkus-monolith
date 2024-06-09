import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISupplier, NewSupplier } from '../supplier.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISupplier for edit and NewSupplierFormGroupInput for create.
 */
type SupplierFormGroupInput = ISupplier | PartialWithRequiredKeyOf<NewSupplier>;

type SupplierFormDefaults = Pick<NewSupplier, 'id'>;

type SupplierFormGroupContent = {
  id: FormControl<ISupplier['id'] | NewSupplier['id']>;
  acronym: FormControl<ISupplier['acronym']>;
  companyName: FormControl<ISupplier['companyName']>;
  streetAddress: FormControl<ISupplier['streetAddress']>;
  houseNumber: FormControl<ISupplier['houseNumber']>;
  locationName: FormControl<ISupplier['locationName']>;
  city: FormControl<ISupplier['city']>;
  stateProvince: FormControl<ISupplier['stateProvince']>;
  zipPostalCode: FormControl<ISupplier['zipPostalCode']>;
  countryRegion: FormControl<ISupplier['countryRegion']>;
  pointLocation: FormControl<ISupplier['pointLocation']>;
  pointLocationContentType: FormControl<ISupplier['pointLocationContentType']>;
  mainEmail: FormControl<ISupplier['mainEmail']>;
  phoneNumber1: FormControl<ISupplier['phoneNumber1']>;
  phoneNumber2: FormControl<ISupplier['phoneNumber2']>;
  customAttributesDetailsJSON: FormControl<ISupplier['customAttributesDetailsJSON']>;
  activationStatus: FormControl<ISupplier['activationStatus']>;
  representativePerson: FormControl<ISupplier['representativePerson']>;
};

export type SupplierFormGroup = FormGroup<SupplierFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SupplierFormService {
  createSupplierFormGroup(supplier: SupplierFormGroupInput = { id: null }): SupplierFormGroup {
    const supplierRawValue = {
      ...this.getFormDefaults(),
      ...supplier,
    };
    return new FormGroup<SupplierFormGroupContent>({
      id: new FormControl(
        { value: supplierRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      acronym: new FormControl(supplierRawValue.acronym, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      companyName: new FormControl(supplierRawValue.companyName, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(120)],
      }),
      streetAddress: new FormControl(supplierRawValue.streetAddress, {
        validators: [Validators.required, Validators.maxLength(120)],
      }),
      houseNumber: new FormControl(supplierRawValue.houseNumber, {
        validators: [Validators.maxLength(20)],
      }),
      locationName: new FormControl(supplierRawValue.locationName, {
        validators: [Validators.maxLength(50)],
      }),
      city: new FormControl(supplierRawValue.city, {
        validators: [Validators.maxLength(50)],
      }),
      stateProvince: new FormControl(supplierRawValue.stateProvince, {
        validators: [Validators.maxLength(50)],
      }),
      zipPostalCode: new FormControl(supplierRawValue.zipPostalCode, {
        validators: [Validators.maxLength(15)],
      }),
      countryRegion: new FormControl(supplierRawValue.countryRegion, {
        validators: [Validators.maxLength(50)],
      }),
      pointLocation: new FormControl(supplierRawValue.pointLocation),
      pointLocationContentType: new FormControl(supplierRawValue.pointLocationContentType),
      mainEmail: new FormControl(supplierRawValue.mainEmail, {
        validators: [Validators.maxLength(120), Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      phoneNumber1: new FormControl(supplierRawValue.phoneNumber1, {
        validators: [Validators.maxLength(15)],
      }),
      phoneNumber2: new FormControl(supplierRawValue.phoneNumber2, {
        validators: [Validators.maxLength(15)],
      }),
      customAttributesDetailsJSON: new FormControl(supplierRawValue.customAttributesDetailsJSON, {
        validators: [Validators.maxLength(2048)],
      }),
      activationStatus: new FormControl(supplierRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      representativePerson: new FormControl(supplierRawValue.representativePerson, {
        validators: [Validators.required],
      }),
    });
  }

  getSupplier(form: SupplierFormGroup): ISupplier | NewSupplier {
    return form.getRawValue() as ISupplier | NewSupplier;
  }

  resetForm(form: SupplierFormGroup, supplier: SupplierFormGroupInput): void {
    const supplierRawValue = { ...this.getFormDefaults(), ...supplier };
    form.reset(
      {
        ...supplierRawValue,
        id: { value: supplierRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SupplierFormDefaults {
    return {
      id: null,
    };
  }
}
