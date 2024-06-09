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
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { WarehouseService } from '../service/warehouse.service';
import { IWarehouse } from '../warehouse.model';
import { WarehouseFormService, WarehouseFormGroup } from './warehouse-form.service';

@Component({
  standalone: true,
  selector: 'jhi-warehouse-update',
  templateUrl: './warehouse-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WarehouseUpdateComponent implements OnInit {
  isSaving = false;
  warehouse: IWarehouse | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected warehouseService = inject(WarehouseService);
  protected warehouseFormService = inject(WarehouseFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WarehouseFormGroup = this.warehouseFormService.createWarehouseFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warehouse }) => {
      this.warehouse = warehouse;
      if (warehouse) {
        this.updateForm(warehouse);
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
    const warehouse = this.warehouseFormService.getWarehouse(this.editForm);
    if (warehouse.id !== null) {
      this.subscribeToSaveResponse(this.warehouseService.update(warehouse));
    } else {
      this.subscribeToSaveResponse(this.warehouseService.create(warehouse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWarehouse>>): void {
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

  protected updateForm(warehouse: IWarehouse): void {
    this.warehouse = warehouse;
    this.warehouseFormService.resetForm(this.editForm, warehouse);
  }
}
