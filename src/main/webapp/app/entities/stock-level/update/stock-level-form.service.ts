import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStockLevel, NewStockLevel } from '../stock-level.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockLevel for edit and NewStockLevelFormGroupInput for create.
 */
type StockLevelFormGroupInput = IStockLevel | PartialWithRequiredKeyOf<NewStockLevel>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IStockLevel | NewStockLevel> = Omit<T, 'lastModifiedDate'> & {
  lastModifiedDate?: string | null;
};

type StockLevelFormRawValue = FormValueOf<IStockLevel>;

type NewStockLevelFormRawValue = FormValueOf<NewStockLevel>;

type StockLevelFormDefaults = Pick<NewStockLevel, 'id' | 'lastModifiedDate'>;

type StockLevelFormGroupContent = {
  id: FormControl<StockLevelFormRawValue['id'] | NewStockLevel['id']>;
  lastModifiedDate: FormControl<StockLevelFormRawValue['lastModifiedDate']>;
  remainingQuantity: FormControl<StockLevelFormRawValue['remainingQuantity']>;
  commonAttributesDetailsJSON: FormControl<StockLevelFormRawValue['commonAttributesDetailsJSON']>;
  warehouse: FormControl<StockLevelFormRawValue['warehouse']>;
  product: FormControl<StockLevelFormRawValue['product']>;
};

export type StockLevelFormGroup = FormGroup<StockLevelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StockLevelFormService {
  createStockLevelFormGroup(stockLevel: StockLevelFormGroupInput = { id: null }): StockLevelFormGroup {
    const stockLevelRawValue = this.convertStockLevelToStockLevelRawValue({
      ...this.getFormDefaults(),
      ...stockLevel,
    });
    return new FormGroup<StockLevelFormGroupContent>({
      id: new FormControl(
        { value: stockLevelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      lastModifiedDate: new FormControl(stockLevelRawValue.lastModifiedDate, {
        validators: [Validators.required],
      }),
      remainingQuantity: new FormControl(stockLevelRawValue.remainingQuantity, {
        validators: [Validators.required],
      }),
      commonAttributesDetailsJSON: new FormControl(stockLevelRawValue.commonAttributesDetailsJSON, {
        validators: [Validators.maxLength(2048)],
      }),
      warehouse: new FormControl(stockLevelRawValue.warehouse, {
        validators: [Validators.required],
      }),
      product: new FormControl(stockLevelRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getStockLevel(form: StockLevelFormGroup): IStockLevel | NewStockLevel {
    return this.convertStockLevelRawValueToStockLevel(form.getRawValue() as StockLevelFormRawValue | NewStockLevelFormRawValue);
  }

  resetForm(form: StockLevelFormGroup, stockLevel: StockLevelFormGroupInput): void {
    const stockLevelRawValue = this.convertStockLevelToStockLevelRawValue({ ...this.getFormDefaults(), ...stockLevel });
    form.reset(
      {
        ...stockLevelRawValue,
        id: { value: stockLevelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StockLevelFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastModifiedDate: currentTime,
    };
  }

  private convertStockLevelRawValueToStockLevel(
    rawStockLevel: StockLevelFormRawValue | NewStockLevelFormRawValue,
  ): IStockLevel | NewStockLevel {
    return {
      ...rawStockLevel,
      lastModifiedDate: dayjs(rawStockLevel.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertStockLevelToStockLevelRawValue(
    stockLevel: IStockLevel | (Partial<NewStockLevel> & StockLevelFormDefaults),
  ): StockLevelFormRawValue | PartialWithRequiredKeyOf<NewStockLevelFormRawValue> {
    return {
      ...stockLevel,
      lastModifiedDate: stockLevel.lastModifiedDate ? stockLevel.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
