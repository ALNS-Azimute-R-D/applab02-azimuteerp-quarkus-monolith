import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'discontinued'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  productSKU: FormControl<IProduct['productSKU']>;
  productName: FormControl<IProduct['productName']>;
  description: FormControl<IProduct['description']>;
  standardCost: FormControl<IProduct['standardCost']>;
  listPrice: FormControl<IProduct['listPrice']>;
  reorderLevel: FormControl<IProduct['reorderLevel']>;
  targetLevel: FormControl<IProduct['targetLevel']>;
  quantityPerUnit: FormControl<IProduct['quantityPerUnit']>;
  discontinued: FormControl<IProduct['discontinued']>;
  minimumReorderQuantity: FormControl<IProduct['minimumReorderQuantity']>;
  suggestedCategory: FormControl<IProduct['suggestedCategory']>;
  attachments: FormControl<IProduct['attachments']>;
  attachmentsContentType: FormControl<IProduct['attachmentsContentType']>;
  supplierIds: FormControl<IProduct['supplierIds']>;
  brand: FormControl<IProduct['brand']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = {
      ...this.getFormDefaults(),
      ...product,
    };
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      productSKU: new FormControl(productRawValue.productSKU, {
        validators: [Validators.maxLength(25)],
      }),
      productName: new FormControl(productRawValue.productName, {
        validators: [Validators.maxLength(50)],
      }),
      description: new FormControl(productRawValue.description),
      standardCost: new FormControl(productRawValue.standardCost),
      listPrice: new FormControl(productRawValue.listPrice, {
        validators: [Validators.required],
      }),
      reorderLevel: new FormControl(productRawValue.reorderLevel),
      targetLevel: new FormControl(productRawValue.targetLevel),
      quantityPerUnit: new FormControl(productRawValue.quantityPerUnit, {
        validators: [Validators.maxLength(50)],
      }),
      discontinued: new FormControl(productRawValue.discontinued, {
        validators: [Validators.required],
      }),
      minimumReorderQuantity: new FormControl(productRawValue.minimumReorderQuantity),
      suggestedCategory: new FormControl(productRawValue.suggestedCategory, {
        validators: [Validators.maxLength(50)],
      }),
      attachments: new FormControl(productRawValue.attachments),
      attachmentsContentType: new FormControl(productRawValue.attachmentsContentType),
      supplierIds: new FormControl(productRawValue.supplierIds),
      brand: new FormControl(productRawValue.brand, {
        validators: [Validators.required],
      }),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return form.getRawValue() as IProduct | NewProduct;
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = { ...this.getFormDefaults(), ...product };
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    return {
      id: null,
      discontinued: false,
    };
  }
}
