import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAsset } from 'app/entities/asset/asset.model';
import { AssetService } from 'app/entities/asset/service/asset.service';
import { IAssetMetadata } from '../asset-metadata.model';
import { AssetMetadataService } from '../service/asset-metadata.service';
import { AssetMetadataFormService, AssetMetadataFormGroup } from './asset-metadata-form.service';

@Component({
  standalone: true,
  selector: 'jhi-asset-metadata-update',
  templateUrl: './asset-metadata-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssetMetadataUpdateComponent implements OnInit {
  isSaving = false;
  assetMetadata: IAssetMetadata | null = null;

  assetsCollection: IAsset[] = [];

  protected assetMetadataService = inject(AssetMetadataService);
  protected assetMetadataFormService = inject(AssetMetadataFormService);
  protected assetService = inject(AssetService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssetMetadataFormGroup = this.assetMetadataFormService.createAssetMetadataFormGroup();

  compareAsset = (o1: IAsset | null, o2: IAsset | null): boolean => this.assetService.compareAsset(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetMetadata }) => {
      this.assetMetadata = assetMetadata;
      if (assetMetadata) {
        this.updateForm(assetMetadata);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetMetadata = this.assetMetadataFormService.getAssetMetadata(this.editForm);
    if (assetMetadata.id !== null) {
      this.subscribeToSaveResponse(this.assetMetadataService.update(assetMetadata));
    } else {
      this.subscribeToSaveResponse(this.assetMetadataService.create(assetMetadata));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetMetadata>>): void {
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

  protected updateForm(assetMetadata: IAssetMetadata): void {
    this.assetMetadata = assetMetadata;
    this.assetMetadataFormService.resetForm(this.editForm, assetMetadata);

    this.assetsCollection = this.assetService.addAssetToCollectionIfMissing<IAsset>(this.assetsCollection, assetMetadata.asset);
  }

  protected loadRelationshipsOptions(): void {
    this.assetService
      .query({ filter: 'assetmetadata-is-null' })
      .pipe(map((res: HttpResponse<IAsset[]>) => res.body ?? []))
      .pipe(map((assets: IAsset[]) => this.assetService.addAssetToCollectionIfMissing<IAsset>(assets, this.assetMetadata?.asset)))
      .subscribe((assets: IAsset[]) => (this.assetsCollection = assets));
  }
}
