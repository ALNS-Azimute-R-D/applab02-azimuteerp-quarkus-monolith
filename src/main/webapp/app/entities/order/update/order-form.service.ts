import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrder, NewOrder } from '../order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrder for edit and NewOrderFormGroupInput for create.
 */
type OrderFormGroupInput = IOrder | PartialWithRequiredKeyOf<NewOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrder | NewOrder> = Omit<T, 'placedDate' | 'estimatedDeliveryDate'> & {
  placedDate?: string | null;
  estimatedDeliveryDate?: string | null;
};

type OrderFormRawValue = FormValueOf<IOrder>;

type NewOrderFormRawValue = FormValueOf<NewOrder>;

type OrderFormDefaults = Pick<NewOrder, 'id' | 'placedDate' | 'estimatedDeliveryDate'>;

type OrderFormGroupContent = {
  id: FormControl<OrderFormRawValue['id'] | NewOrder['id']>;
  businessCode: FormControl<OrderFormRawValue['businessCode']>;
  placedDate: FormControl<OrderFormRawValue['placedDate']>;
  totalTaxValue: FormControl<OrderFormRawValue['totalTaxValue']>;
  totalDueValue: FormControl<OrderFormRawValue['totalDueValue']>;
  status: FormControl<OrderFormRawValue['status']>;
  estimatedDeliveryDate: FormControl<OrderFormRawValue['estimatedDeliveryDate']>;
  activationStatus: FormControl<OrderFormRawValue['activationStatus']>;
  invoice: FormControl<OrderFormRawValue['invoice']>;
  customer: FormControl<OrderFormRawValue['customer']>;
};

export type OrderFormGroup = FormGroup<OrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderFormService {
  createOrderFormGroup(order: OrderFormGroupInput = { id: null }): OrderFormGroup {
    const orderRawValue = this.convertOrderToOrderRawValue({
      ...this.getFormDefaults(),
      ...order,
    });
    return new FormGroup<OrderFormGroupContent>({
      id: new FormControl(
        { value: orderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      businessCode: new FormControl(orderRawValue.businessCode, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      placedDate: new FormControl(orderRawValue.placedDate, {
        validators: [Validators.required],
      }),
      totalTaxValue: new FormControl(orderRawValue.totalTaxValue),
      totalDueValue: new FormControl(orderRawValue.totalDueValue),
      status: new FormControl(orderRawValue.status, {
        validators: [Validators.required],
      }),
      estimatedDeliveryDate: new FormControl(orderRawValue.estimatedDeliveryDate),
      activationStatus: new FormControl(orderRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      invoice: new FormControl(orderRawValue.invoice),
      customer: new FormControl(orderRawValue.customer, {
        validators: [Validators.required],
      }),
    });
  }

  getOrder(form: OrderFormGroup): IOrder | NewOrder {
    return this.convertOrderRawValueToOrder(form.getRawValue() as OrderFormRawValue | NewOrderFormRawValue);
  }

  resetForm(form: OrderFormGroup, order: OrderFormGroupInput): void {
    const orderRawValue = this.convertOrderToOrderRawValue({ ...this.getFormDefaults(), ...order });
    form.reset(
      {
        ...orderRawValue,
        id: { value: orderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      placedDate: currentTime,
      estimatedDeliveryDate: currentTime,
    };
  }

  private convertOrderRawValueToOrder(rawOrder: OrderFormRawValue | NewOrderFormRawValue): IOrder | NewOrder {
    return {
      ...rawOrder,
      placedDate: dayjs(rawOrder.placedDate, DATE_TIME_FORMAT),
      estimatedDeliveryDate: dayjs(rawOrder.estimatedDeliveryDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrderToOrderRawValue(
    order: IOrder | (Partial<NewOrder> & OrderFormDefaults),
  ): OrderFormRawValue | PartialWithRequiredKeyOf<NewOrderFormRawValue> {
    return {
      ...order,
      placedDate: order.placedDate ? order.placedDate.format(DATE_TIME_FORMAT) : undefined,
      estimatedDeliveryDate: order.estimatedDeliveryDate ? order.estimatedDeliveryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
