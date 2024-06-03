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
import { StatusRawProcessingEnum } from 'app/entities/enumerations/status-raw-processing-enum.model';
import { RawAssetProcTmpService } from '../service/raw-asset-proc-tmp.service';
import { IRawAssetProcTmp } from '../raw-asset-proc-tmp.model';
import { RawAssetProcTmpFormService, RawAssetProcTmpFormGroup } from './raw-asset-proc-tmp-form.service';

@Component({
  standalone: true,
  selector: 'jhi-raw-asset-proc-tmp-update',
  templateUrl: './raw-asset-proc-tmp-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RawAssetProcTmpUpdateComponent implements OnInit {
  isSaving = false;
  rawAssetProcTmp: IRawAssetProcTmp | null = null;
  statusRawProcessingEnumValues = Object.keys(StatusRawProcessingEnum);

  assetTypesSharedCollection: IAssetType[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected rawAssetProcTmpService = inject(RawAssetProcTmpService);
  protected rawAssetProcTmpFormService = inject(RawAssetProcTmpFormService);
  protected assetTypeService = inject(AssetTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RawAssetProcTmpFormGroup = this.rawAssetProcTmpFormService.createRawAssetProcTmpFormGroup();

  compareAssetType = (o1: IAssetType | null, o2: IAssetType | null): boolean => this.assetTypeService.compareAssetType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rawAssetProcTmp }) => {
      this.rawAssetProcTmp = rawAssetProcTmp;
      if (rawAssetProcTmp) {
        this.updateForm(rawAssetProcTmp);
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
    const rawAssetProcTmp = this.rawAssetProcTmpFormService.getRawAssetProcTmp(this.editForm);
    if (rawAssetProcTmp.id !== null) {
      this.subscribeToSaveResponse(this.rawAssetProcTmpService.update(rawAssetProcTmp));
    } else {
      this.subscribeToSaveResponse(this.rawAssetProcTmpService.create(rawAssetProcTmp));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRawAssetProcTmp>>): void {
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

  protected updateForm(rawAssetProcTmp: IRawAssetProcTmp): void {
    this.rawAssetProcTmp = rawAssetProcTmp;
    this.rawAssetProcTmpFormService.resetForm(this.editForm, rawAssetProcTmp);

    this.assetTypesSharedCollection = this.assetTypeService.addAssetTypeToCollectionIfMissing<IAssetType>(
      this.assetTypesSharedCollection,
      rawAssetProcTmp.assetType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.assetTypeService
      .query()
      .pipe(map((res: HttpResponse<IAssetType[]>) => res.body ?? []))
      .pipe(
        map((assetTypes: IAssetType[]) =>
          this.assetTypeService.addAssetTypeToCollectionIfMissing<IAssetType>(assetTypes, this.rawAssetProcTmp?.assetType),
        ),
      )
      .subscribe((assetTypes: IAssetType[]) => (this.assetTypesSharedCollection = assetTypes));
  }
}
