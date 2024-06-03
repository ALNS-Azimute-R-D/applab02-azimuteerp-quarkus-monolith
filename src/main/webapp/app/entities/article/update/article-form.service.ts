import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IArticle, NewArticle } from '../article.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArticle for edit and NewArticleFormGroupInput for create.
 */
type ArticleFormGroupInput = IArticle | PartialWithRequiredKeyOf<NewArticle>;

type ArticleFormDefaults = Pick<NewArticle, 'id' | 'isEnabled'>;

type ArticleFormGroupContent = {
  id: FormControl<IArticle['id'] | NewArticle['id']>;
  inventoryProductId: FormControl<IArticle['inventoryProductId']>;
  customName: FormControl<IArticle['customName']>;
  customDescription: FormControl<IArticle['customDescription']>;
  priceValue: FormControl<IArticle['priceValue']>;
  itemSize: FormControl<IArticle['itemSize']>;
  assetsCollectionUUID: FormControl<IArticle['assetsCollectionUUID']>;
  isEnabled: FormControl<IArticle['isEnabled']>;
  mainCategory: FormControl<IArticle['mainCategory']>;
};

export type ArticleFormGroup = FormGroup<ArticleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArticleFormService {
  createArticleFormGroup(article: ArticleFormGroupInput = { id: null }): ArticleFormGroup {
    const articleRawValue = {
      ...this.getFormDefaults(),
      ...article,
    };
    return new FormGroup<ArticleFormGroupContent>({
      id: new FormControl(
        { value: articleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      inventoryProductId: new FormControl(articleRawValue.inventoryProductId, {
        validators: [Validators.required],
      }),
      customName: new FormControl(articleRawValue.customName, {
        validators: [Validators.maxLength(150)],
      }),
      customDescription: new FormControl(articleRawValue.customDescription),
      priceValue: new FormControl(articleRawValue.priceValue),
      itemSize: new FormControl(articleRawValue.itemSize, {
        validators: [Validators.required],
      }),
      assetsCollectionUUID: new FormControl(articleRawValue.assetsCollectionUUID, {
        validators: [Validators.maxLength(255)],
      }),
      isEnabled: new FormControl(articleRawValue.isEnabled),
      mainCategory: new FormControl(articleRawValue.mainCategory),
    });
  }

  getArticle(form: ArticleFormGroup): IArticle | NewArticle {
    return form.getRawValue() as IArticle | NewArticle;
  }

  resetForm(form: ArticleFormGroup, article: ArticleFormGroupInput): void {
    const articleRawValue = { ...this.getFormDefaults(), ...article };
    form.reset(
      {
        ...articleRawValue,
        id: { value: articleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ArticleFormDefaults {
    return {
      id: null,
      isEnabled: false,
    };
  }
}
