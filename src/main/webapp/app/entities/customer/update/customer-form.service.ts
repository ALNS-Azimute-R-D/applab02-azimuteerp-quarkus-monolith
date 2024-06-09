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

type CustomerFormDefaults = Pick<NewCustomer, 'id'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  customerBusinessCode: FormControl<ICustomer['customerBusinessCode']>;
  fullname: FormControl<ICustomer['fullname']>;
  customAttributesDetailsJSON: FormControl<ICustomer['customAttributesDetailsJSON']>;
  customerStatus: FormControl<ICustomer['customerStatus']>;
  activationStatus: FormControl<ICustomer['activationStatus']>;
  buyerPerson: FormControl<ICustomer['buyerPerson']>;
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
      fullname: new FormControl(customerRawValue.fullname, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(80)],
      }),
      customAttributesDetailsJSON: new FormControl(customerRawValue.customAttributesDetailsJSON, {
        validators: [Validators.maxLength(2048)],
      }),
      customerStatus: new FormControl(customerRawValue.customerStatus, {
        validators: [Validators.required],
      }),
      activationStatus: new FormControl(customerRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      buyerPerson: new FormControl(customerRawValue.buyerPerson, {
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
    };
  }
}
