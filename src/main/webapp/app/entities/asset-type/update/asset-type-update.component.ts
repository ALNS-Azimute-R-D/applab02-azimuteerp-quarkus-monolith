import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAssetType } from '../asset-type.model';
import { AssetTypeService } from '../service/asset-type.service';
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
