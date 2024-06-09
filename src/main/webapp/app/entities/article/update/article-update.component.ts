import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssetCollection } from 'app/entities/asset-collection/asset-collection.model';
import { AssetCollectionService } from 'app/entities/asset-collection/service/asset-collection.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { SizeOptionEnum } from 'app/entities/enumerations/size-option-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { ArticleService } from '../service/article.service';
import { IArticle } from '../article.model';
import { ArticleFormService, ArticleFormGroup } from './article-form.service';

@Component({
  standalone: true,
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;
  article: IArticle | null = null;
  sizeOptionEnumValues = Object.keys(SizeOptionEnum);
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  assetCollectionsSharedCollection: IAssetCollection[] = [];
  categoriesSharedCollection: ICategory[] = [];

  protected articleService = inject(ArticleService);
  protected articleFormService = inject(ArticleFormService);
  protected assetCollectionService = inject(AssetCollectionService);
  protected categoryService = inject(CategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ArticleFormGroup = this.articleFormService.createArticleFormGroup();

  compareAssetCollection = (o1: IAssetCollection | null, o2: IAssetCollection | null): boolean =>
    this.assetCollectionService.compareAssetCollection(o1, o2);

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      this.article = article;
      if (article) {
        this.updateForm(article);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const article = this.articleFormService.getArticle(this.editForm);
    if (article.id !== null) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(article: IArticle): void {
    this.article = article;
    this.articleFormService.resetForm(this.editForm, article);

    this.assetCollectionsSharedCollection = this.assetCollectionService.addAssetCollectionToCollectionIfMissing<IAssetCollection>(
      this.assetCollectionsSharedCollection,
      ...(article.assetCollections ?? []),
    );
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      article.mainCategory,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assetCollectionService
      .query()
      .pipe(map((res: HttpResponse<IAssetCollection[]>) => res.body ?? []))
      .pipe(
        map((assetCollections: IAssetCollection[]) =>
          this.assetCollectionService.addAssetCollectionToCollectionIfMissing<IAssetCollection>(
            assetCollections,
            ...(this.article?.assetCollections ?? []),
          ),
        ),
      )
      .subscribe((assetCollections: IAssetCollection[]) => (this.assetCollectionsSharedCollection = assetCollections));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.article?.mainCategory),
        ),
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }
}
