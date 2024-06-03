import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPayment, NewPayment } from '../payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayment for edit and NewPaymentFormGroupInput for create.
 */
type PaymentFormGroupInput = IPayment | PartialWithRequiredKeyOf<NewPayment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPayment | NewPayment> = Omit<T, 'paymentDueDate' | 'paymentPaidDate'> & {
  paymentDueDate?: string | null;
  paymentPaidDate?: string | null;
};

type PaymentFormRawValue = FormValueOf<IPayment>;

type NewPaymentFormRawValue = FormValueOf<NewPayment>;

type PaymentFormDefaults = Pick<NewPayment, 'id' | 'paymentDueDate' | 'paymentPaidDate'>;

type PaymentFormGroupContent = {
  id: FormControl<PaymentFormRawValue['id'] | NewPayment['id']>;
  installmentNumber: FormControl<PaymentFormRawValue['installmentNumber']>;
  paymentDueDate: FormControl<PaymentFormRawValue['paymentDueDate']>;
  paymentPaidDate: FormControl<PaymentFormRawValue['paymentPaidDate']>;
  paymentAmount: FormControl<PaymentFormRawValue['paymentAmount']>;
  typeOfPayment: FormControl<PaymentFormRawValue['typeOfPayment']>;
  status: FormControl<PaymentFormRawValue['status']>;
  extraDetails: FormControl<PaymentFormRawValue['extraDetails']>;
  paymentMethod: FormControl<PaymentFormRawValue['paymentMethod']>;
};

export type PaymentFormGroup = FormGroup<PaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentFormService {
  createPaymentFormGroup(payment: PaymentFormGroupInput = { id: null }): PaymentFormGroup {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({
      ...this.getFormDefaults(),
      ...payment,
    });
    return new FormGroup<PaymentFormGroupContent>({
      id: new FormControl(
        { value: paymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      installmentNumber: new FormControl(paymentRawValue.installmentNumber, {
        validators: [Validators.required],
      }),
      paymentDueDate: new FormControl(paymentRawValue.paymentDueDate, {
        validators: [Validators.required],
      }),
      paymentPaidDate: new FormControl(paymentRawValue.paymentPaidDate, {
        validators: [Validators.required],
      }),
      paymentAmount: new FormControl(paymentRawValue.paymentAmount, {
        validators: [Validators.required],
      }),
      typeOfPayment: new FormControl(paymentRawValue.typeOfPayment, {
        validators: [Validators.required],
      }),
      status: new FormControl(paymentRawValue.status, {
        validators: [Validators.required],
      }),
      extraDetails: new FormControl(paymentRawValue.extraDetails),
      paymentMethod: new FormControl(paymentRawValue.paymentMethod, {
        validators: [Validators.required],
      }),
    });
  }

  getPayment(form: PaymentFormGroup): IPayment | NewPayment {
    return this.convertPaymentRawValueToPayment(form.getRawValue() as PaymentFormRawValue | NewPaymentFormRawValue);
  }

  resetForm(form: PaymentFormGroup, payment: PaymentFormGroupInput): void {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({ ...this.getFormDefaults(), ...payment });
    form.reset(
      {
        ...paymentRawValue,
        id: { value: paymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaymentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      paymentDueDate: currentTime,
      paymentPaidDate: currentTime,
    };
  }

  private convertPaymentRawValueToPayment(rawPayment: PaymentFormRawValue | NewPaymentFormRawValue): IPayment | NewPayment {
    return {
      ...rawPayment,
      paymentDueDate: dayjs(rawPayment.paymentDueDate, DATE_TIME_FORMAT),
      paymentPaidDate: dayjs(rawPayment.paymentPaidDate, DATE_TIME_FORMAT),
    };
  }

  private convertPaymentToPaymentRawValue(
    payment: IPayment | (Partial<NewPayment> & PaymentFormDefaults),
  ): PaymentFormRawValue | PartialWithRequiredKeyOf<NewPaymentFormRawValue> {
    return {
      ...payment,
      paymentDueDate: payment.paymentDueDate ? payment.paymentDueDate.format(DATE_TIME_FORMAT) : undefined,
      paymentPaidDate: payment.paymentPaidDate ? payment.paymentPaidDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
