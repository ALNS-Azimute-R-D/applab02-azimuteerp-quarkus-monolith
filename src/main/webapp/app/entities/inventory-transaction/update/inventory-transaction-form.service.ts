import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInventoryTransaction, NewInventoryTransaction } from '../inventory-transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventoryTransaction for edit and NewInventoryTransactionFormGroupInput for create.
 */
type InventoryTransactionFormGroupInput = IInventoryTransaction | PartialWithRequiredKeyOf<NewInventoryTransaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInventoryTransaction | NewInventoryTransaction> = Omit<
  T,
  'transactionCreatedDate' | 'transactionModifiedDate'
> & {
  transactionCreatedDate?: string | null;
  transactionModifiedDate?: string | null;
};

type InventoryTransactionFormRawValue = FormValueOf<IInventoryTransaction>;

type NewInventoryTransactionFormRawValue = FormValueOf<NewInventoryTransaction>;

type InventoryTransactionFormDefaults = Pick<NewInventoryTransaction, 'id' | 'transactionCreatedDate' | 'transactionModifiedDate'>;

type InventoryTransactionFormGroupContent = {
  id: FormControl<InventoryTransactionFormRawValue['id'] | NewInventoryTransaction['id']>;
  invoiceId: FormControl<InventoryTransactionFormRawValue['invoiceId']>;
  transactionCreatedDate: FormControl<InventoryTransactionFormRawValue['transactionCreatedDate']>;
  transactionModifiedDate: FormControl<InventoryTransactionFormRawValue['transactionModifiedDate']>;
  quantity: FormControl<InventoryTransactionFormRawValue['quantity']>;
  comments: FormControl<InventoryTransactionFormRawValue['comments']>;
  supplier: FormControl<InventoryTransactionFormRawValue['supplier']>;
  product: FormControl<InventoryTransactionFormRawValue['product']>;
  warehouse: FormControl<InventoryTransactionFormRawValue['warehouse']>;
};

export type InventoryTransactionFormGroup = FormGroup<InventoryTransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InventoryTransactionFormService {
  createInventoryTransactionFormGroup(
    inventoryTransaction: InventoryTransactionFormGroupInput = { id: null },
  ): InventoryTransactionFormGroup {
    const inventoryTransactionRawValue = this.convertInventoryTransactionToInventoryTransactionRawValue({
      ...this.getFormDefaults(),
      ...inventoryTransaction,
    });
    return new FormGroup<InventoryTransactionFormGroupContent>({
      id: new FormControl(
        { value: inventoryTransactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoiceId: new FormControl(inventoryTransactionRawValue.invoiceId, {
        validators: [Validators.required],
      }),
      transactionCreatedDate: new FormControl(inventoryTransactionRawValue.transactionCreatedDate),
      transactionModifiedDate: new FormControl(inventoryTransactionRawValue.transactionModifiedDate),
      quantity: new FormControl(inventoryTransactionRawValue.quantity, {
        validators: [Validators.required],
      }),
      comments: new FormControl(inventoryTransactionRawValue.comments, {
        validators: [Validators.maxLength(255)],
      }),
      supplier: new FormControl(inventoryTransactionRawValue.supplier, {
        validators: [Validators.required],
      }),
      product: new FormControl(inventoryTransactionRawValue.product, {
        validators: [Validators.required],
      }),
      warehouse: new FormControl(inventoryTransactionRawValue.warehouse, {
        validators: [Validators.required],
      }),
    });
  }

  getInventoryTransaction(form: InventoryTransactionFormGroup): IInventoryTransaction | NewInventoryTransaction {
    return this.convertInventoryTransactionRawValueToInventoryTransaction(
      form.getRawValue() as InventoryTransactionFormRawValue | NewInventoryTransactionFormRawValue,
    );
  }

  resetForm(form: InventoryTransactionFormGroup, inventoryTransaction: InventoryTransactionFormGroupInput): void {
    const inventoryTransactionRawValue = this.convertInventoryTransactionToInventoryTransactionRawValue({
      ...this.getFormDefaults(),
      ...inventoryTransaction,
    });
    form.reset(
      {
        ...inventoryTransactionRawValue,
        id: { value: inventoryTransactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InventoryTransactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      transactionCreatedDate: currentTime,
      transactionModifiedDate: currentTime,
    };
  }

  private convertInventoryTransactionRawValueToInventoryTransaction(
    rawInventoryTransaction: InventoryTransactionFormRawValue | NewInventoryTransactionFormRawValue,
  ): IInventoryTransaction | NewInventoryTransaction {
    return {
      ...rawInventoryTransaction,
      transactionCreatedDate: dayjs(rawInventoryTransaction.transactionCreatedDate, DATE_TIME_FORMAT),
      transactionModifiedDate: dayjs(rawInventoryTransaction.transactionModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertInventoryTransactionToInventoryTransactionRawValue(
    inventoryTransaction: IInventoryTransaction | (Partial<NewInventoryTransaction> & InventoryTransactionFormDefaults),
  ): InventoryTransactionFormRawValue | PartialWithRequiredKeyOf<NewInventoryTransactionFormRawValue> {
    return {
      ...inventoryTransaction,
      transactionCreatedDate: inventoryTransaction.transactionCreatedDate
        ? inventoryTransaction.transactionCreatedDate.format(DATE_TIME_FORMAT)
        : undefined,
      transactionModifiedDate: inventoryTransaction.transactionModifiedDate
        ? inventoryTransaction.transactionModifiedDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
