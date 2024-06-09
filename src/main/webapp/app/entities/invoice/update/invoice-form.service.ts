import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'invoiceDate' | 'dueDate'> & {
  invoiceDate?: string | null;
  dueDate?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'invoiceDate' | 'dueDate'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  businessCode: FormControl<InvoiceFormRawValue['businessCode']>;
  invoiceDate: FormControl<InvoiceFormRawValue['invoiceDate']>;
  dueDate: FormControl<InvoiceFormRawValue['dueDate']>;
  description: FormControl<InvoiceFormRawValue['description']>;
  taxValue: FormControl<InvoiceFormRawValue['taxValue']>;
  shippingValue: FormControl<InvoiceFormRawValue['shippingValue']>;
  amountDueValue: FormControl<InvoiceFormRawValue['amountDueValue']>;
  numberOfInstallmentsOriginal: FormControl<InvoiceFormRawValue['numberOfInstallmentsOriginal']>;
  numberOfInstallmentsPaid: FormControl<InvoiceFormRawValue['numberOfInstallmentsPaid']>;
  amountPaidValue: FormControl<InvoiceFormRawValue['amountPaidValue']>;
  status: FormControl<InvoiceFormRawValue['status']>;
  customAttributesDetailsJSON: FormControl<InvoiceFormRawValue['customAttributesDetailsJSON']>;
  activationStatus: FormControl<InvoiceFormRawValue['activationStatus']>;
  preferrablePaymentGateway: FormControl<InvoiceFormRawValue['preferrablePaymentGateway']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      businessCode: new FormControl(invoiceRawValue.businessCode, {
        validators: [Validators.required, Validators.maxLength(15)],
      }),
      invoiceDate: new FormControl(invoiceRawValue.invoiceDate),
      dueDate: new FormControl(invoiceRawValue.dueDate),
      description: new FormControl(invoiceRawValue.description, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      taxValue: new FormControl(invoiceRawValue.taxValue),
      shippingValue: new FormControl(invoiceRawValue.shippingValue),
      amountDueValue: new FormControl(invoiceRawValue.amountDueValue),
      numberOfInstallmentsOriginal: new FormControl(invoiceRawValue.numberOfInstallmentsOriginal, {
        validators: [Validators.required],
      }),
      numberOfInstallmentsPaid: new FormControl(invoiceRawValue.numberOfInstallmentsPaid),
      amountPaidValue: new FormControl(invoiceRawValue.amountPaidValue),
      status: new FormControl(invoiceRawValue.status, {
        validators: [Validators.required],
      }),
      customAttributesDetailsJSON: new FormControl(invoiceRawValue.customAttributesDetailsJSON, {
        validators: [Validators.maxLength(4096)],
      }),
      activationStatus: new FormControl(invoiceRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      preferrablePaymentGateway: new FormControl(invoiceRawValue.preferrablePaymentGateway),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      invoiceDate: currentTime,
      dueDate: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      invoiceDate: dayjs(rawInvoice.invoiceDate, DATE_TIME_FORMAT),
      dueDate: dayjs(rawInvoice.dueDate, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults),
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      invoiceDate: invoice.invoiceDate ? invoice.invoiceDate.format(DATE_TIME_FORMAT) : undefined,
      dueDate: invoice.dueDate ? invoice.dueDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
