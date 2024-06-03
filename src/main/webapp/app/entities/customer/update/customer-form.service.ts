import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id' | 'active'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  customerBusinessCode: FormControl<ICustomer['customerBusinessCode']>;
  name: FormControl<ICustomer['name']>;
  description: FormControl<ICustomer['description']>;
  email: FormControl<ICustomer['email']>;
  addressDetails: FormControl<ICustomer['addressDetails']>;
  zipCode: FormControl<ICustomer['zipCode']>;
  keycloakGroupDetails: FormControl<ICustomer['keycloakGroupDetails']>;
  status: FormControl<ICustomer['status']>;
  active: FormControl<ICustomer['active']>;
  customerType: FormControl<ICustomer['customerType']>;
  district: FormControl<ICustomer['district']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = {
      ...this.getFormDefaults(),
      ...customer,
    };
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      customerBusinessCode: new FormControl(customerRawValue.customerBusinessCode, {
        validators: [Validators.required, Validators.maxLength(15)],
      }),
      name: new FormControl(customerRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(80)],
      }),
      description: new FormControl(customerRawValue.description),
      email: new FormControl(customerRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      addressDetails: new FormControl(customerRawValue.addressDetails, {
        validators: [Validators.maxLength(255)],
      }),
      zipCode: new FormControl(customerRawValue.zipCode, {
        validators: [Validators.maxLength(8)],
      }),
      keycloakGroupDetails: new FormControl(customerRawValue.keycloakGroupDetails, {
        validators: [Validators.maxLength(1024)],
      }),
      status: new FormControl(customerRawValue.status, {
        validators: [Validators.required],
      }),
      active: new FormControl(customerRawValue.active, {
        validators: [Validators.required],
      }),
      customerType: new FormControl(customerRawValue.customerType),
      district: new FormControl(customerRawValue.district),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return form.getRawValue() as ICustomer | NewCustomer;
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = { ...this.getFormDefaults(), ...customer };
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
