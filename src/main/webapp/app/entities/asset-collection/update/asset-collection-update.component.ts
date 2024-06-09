import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAsset } from 'app/entities/asset/asset.model';
import { AssetService } from 'app/entities/asset/service/asset.service';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { AssetCollectionService } from '../service/asset-collection.service';
import { IAssetCollection } from '../asset-collection.model';
import { AssetCollectionFormService, AssetCollectionFormGroup } from './asset-collection-form.service';

@Component({
  standalone: true,
  selector: 'jhi-asset-collection-update',
  templateUrl: './asset-collection-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssetCollectionUpdateComponent implements OnInit {
  isSaving = false;
  assetCollection: IAssetCollection | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  assetsSharedCollection: IAsset[] = [];

  protected assetCollectionService = inject(AssetCollectionService);
  protected assetCollectionFormService = inject(AssetCollectionFormService);
  protected assetService = inject(AssetService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssetCollectionFormGroup = this.assetCollectionFormService.createAssetCollectionFormGroup();

  compareAsset = (o1: IAsset | null, o2: IAsset | null): boolean => this.assetService.compareAsset(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetCollection }) => {
      this.assetCollection = assetCollection;
      if (assetCollection) {
        this.updateForm(assetCollection);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetCollection = this.assetCollectionFormService.getAssetCollection(this.editForm);
    if (assetCollection.id !== null) {
      this.subscribeToSaveResponse(this.assetCollectionService.update(assetCollection));
    } else {
      this.subscribeToSaveResponse(this.assetCollectionService.create(assetCollection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetCollection>>): void {
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

  protected updateForm(assetCollection: IAssetCollection): void {
    this.assetCollection = assetCollection;
    this.assetCollectionFormService.resetForm(this.editForm, assetCollection);

    this.assetsSharedCollection = this.assetService.addAssetToCollectionIfMissing<IAsset>(
      this.assetsSharedCollection,
      ...(assetCollection.assets ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assetService
      .query()
      .pipe(map((res: HttpResponse<IAsset[]>) => res.body ?? []))
      .pipe(
        map((assets: IAsset[]) => this.assetService.addAssetToCollectionIfMissing<IAsset>(assets, ...(this.assetCollection?.assets ?? []))),
      )
      .subscribe((assets: IAsset[]) => (this.assetsSharedCollection = assets));
  }
}
