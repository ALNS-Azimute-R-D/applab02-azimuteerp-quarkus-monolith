import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAssetType } from 'app/entities/asset-type/asset-type.model';
import { AssetTypeService } from 'app/entities/asset-type/service/asset-type.service';
import { IRawAssetProcTmp } from 'app/entities/raw-asset-proc-tmp/raw-asset-proc-tmp.model';
import { RawAssetProcTmpService } from 'app/entities/raw-asset-proc-tmp/service/raw-asset-proc-tmp.service';
import { StorageTypeEnum } from 'app/entities/enumerations/storage-type-enum.model';
import { StatusAssetEnum } from 'app/entities/enumerations/status-asset-enum.model';
import { PreferredPurposeEnum } from 'app/entities/enumerations/preferred-purpose-enum.model';
import { AssetService } from '../service/asset.service';
import { IAsset } from '../asset.model';
import { AssetFormService, AssetFormGroup } from './asset-form.service';

@Component({
  standalone: true,
  selector: 'jhi-asset-update',
  templateUrl: './asset-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssetUpdateComponent implements OnInit {
  isSaving = false;
  asset: IAsset | null = null;
  storageTypeEnumValues = Object.keys(StorageTypeEnum);
  statusAssetEnumValues = Object.keys(StatusAssetEnum);
  preferredPurposeEnumValues = Object.keys(PreferredPurposeEnum);

  assetTypesSharedCollection: IAssetType[] = [];
  rawAssetProcTmpsSharedCollection: IRawAssetProcTmp[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected assetService = inject(AssetService);
  protected assetFormService = inject(AssetFormService);
  protected assetTypeService = inject(AssetTypeService);
  protected rawAssetProcTmpService = inject(RawAssetProcTmpService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssetFormGroup = this.assetFormService.createAssetFormGroup();

  compareAssetType = (o1: IAssetType | null, o2: IAssetType | null): boolean => this.assetTypeService.compareAssetType(o1, o2);

  compareRawAssetProcTmp = (o1: IRawAssetProcTmp | null, o2: IRawAssetProcTmp | null): boolean =>
    this.rawAssetProcTmpService.compareRawAssetProcTmp(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ asset }) => {
      this.asset = asset;
      if (asset) {
        this.updateForm(asset);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('azimuteErpQuarkusAngularMonolith02App.error', { ...err, key: 'error.file.' + err.key }),
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const asset = this.assetFormService.getAsset(this.editForm);
    if (asset.id !== null) {
      this.subscribeToSaveResponse(this.assetService.update(asset));
    } else {
      this.subscribeToSaveResponse(this.assetService.create(asset));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAsset>>): void {
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

  protected updateForm(asset: IAsset): void {
    this.asset = asset;
    this.assetFormService.resetForm(this.editForm, asset);

    this.assetTypesSharedCollection = this.assetTypeService.addAssetTypeToCollectionIfMissing<IAssetType>(
      this.assetTypesSharedCollection,
      asset.assetType,
    );
    this.rawAssetProcTmpsSharedCollection = this.rawAssetProcTmpService.addRawAssetProcTmpToCollectionIfMissing<IRawAssetProcTmp>(
      this.rawAssetProcTmpsSharedCollection,
      asset.rawAssetProcTmp,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assetTypeService
      .query()
      .pipe(map((res: HttpResponse<IAssetType[]>) => res.body ?? []))
      .pipe(
        map((assetTypes: IAssetType[]) =>
          this.assetTypeService.addAssetTypeToCollectionIfMissing<IAssetType>(assetTypes, this.asset?.assetType),
        ),
      )
      .subscribe((assetTypes: IAssetType[]) => (this.assetTypesSharedCollection = assetTypes));

    this.rawAssetProcTmpService
      .query()
      .pipe(map((res: HttpResponse<IRawAssetProcTmp[]>) => res.body ?? []))
      .pipe(
        map((rawAssetProcTmps: IRawAssetProcTmp[]) =>
          this.rawAssetProcTmpService.addRawAssetProcTmpToCollectionIfMissing<IRawAssetProcTmp>(
            rawAssetProcTmps,
            this.asset?.rawAssetProcTmp,
          ),
        ),
      )
      .subscribe((rawAssetProcTmps: IRawAssetProcTmp[]) => (this.rawAssetProcTmpsSharedCollection = rawAssetProcTmps));
  }
}
