import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaymentGateway, NewPaymentGateway } from '../payment-gateway.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentGateway for edit and NewPaymentGatewayFormGroupInput for create.
 */
type PaymentGatewayFormGroupInput = IPaymentGateway | PartialWithRequiredKeyOf<NewPaymentGateway>;

type PaymentGatewayFormDefaults = Pick<NewPaymentGateway, 'id'>;

type PaymentGatewayFormGroupContent = {
  id: FormControl<IPaymentGateway['id'] | NewPaymentGateway['id']>;
  aliasCode: FormControl<IPaymentGateway['aliasCode']>;
  description: FormControl<IPaymentGateway['description']>;
  businessHandlerClazz: FormControl<IPaymentGateway['businessHandlerClazz']>;
  activationStatus: FormControl<IPaymentGateway['activationStatus']>;
};

export type PaymentGatewayFormGroup = FormGroup<PaymentGatewayFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentGatewayFormService {
  createPaymentGatewayFormGroup(paymentGateway: PaymentGatewayFormGroupInput = { id: null }): PaymentGatewayFormGroup {
    const paymentGatewayRawValue = {
      ...this.getFormDefaults(),
      ...paymentGateway,
    };
    return new FormGroup<PaymentGatewayFormGroupContent>({
      id: new FormControl(
        { value: paymentGatewayRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      aliasCode: new FormControl(paymentGatewayRawValue.aliasCode, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      description: new FormControl(paymentGatewayRawValue.description, {
        validators: [Validators.required, Validators.maxLength(120)],
      }),
      businessHandlerClazz: new FormControl(paymentGatewayRawValue.businessHandlerClazz, {
        validators: [Validators.maxLength(512)],
      }),
      activationStatus: new FormControl(paymentGatewayRawValue.activationStatus, {
        validators: [Validators.required],
      }),
    });
  }

  getPaymentGateway(form: PaymentGatewayFormGroup): IPaymentGateway | NewPaymentGateway {
    return form.getRawValue() as IPaymentGateway | NewPaymentGateway;
  }

  resetForm(form: PaymentGatewayFormGroup, paymentGateway: PaymentGatewayFormGroupInput): void {
    const paymentGatewayRawValue = { ...this.getFormDefaults(), ...paymentGateway };
    form.reset(
      {
        ...paymentGatewayRawValue,
        id: { value: paymentGatewayRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaymentGatewayFormDefaults {
    return {
      id: null,
    };
  }
}
