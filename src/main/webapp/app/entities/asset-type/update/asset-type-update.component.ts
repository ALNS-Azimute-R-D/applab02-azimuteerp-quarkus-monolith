import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { AssetTypeService } from '../service/asset-type.service';
import { IAssetType } from '../asset-type.model';
import { AssetTypeFormService, AssetTypeFormGroup } from './asset-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-asset-type-update',
  templateUrl: './asset-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AssetTypeUpdateComponent implements OnInit {
  isSaving = false;
  assetType: IAssetType | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected assetTypeService = inject(AssetTypeService);
  protected assetTypeFormService = inject(AssetTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AssetTypeFormGroup = this.assetTypeFormService.createAssetTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetType }) => {
      this.assetType = assetType;
      if (assetType) {
        this.updateForm(assetType);
      }
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
    const assetType = this.assetTypeFormService.getAssetType(this.editForm);
    if (assetType.id !== null) {
      this.subscribeToSaveResponse(this.assetTypeService.update(assetType));
    } else {
      this.subscribeToSaveResponse(this.assetTypeService.create(assetType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetType>>): void {
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

  protected updateForm(assetType: IAssetType): void {
    this.assetType = assetType;
    this.assetTypeFormService.resetForm(this.editForm, assetType);
  }
}
