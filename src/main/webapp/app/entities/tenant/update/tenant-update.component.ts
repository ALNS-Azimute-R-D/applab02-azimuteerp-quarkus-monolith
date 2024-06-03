import { Component, inject, OnInit, ElementRef } from '@angular/core';
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
import { TenantService } from '../service/tenant.service';
import { ITenant } from '../tenant.model';
import { TenantFormService, TenantFormGroup } from './tenant-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tenant-update',
  templateUrl: './tenant-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TenantUpdateComponent implements OnInit {
  isSaving = false;
  tenant: ITenant | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected tenantService = inject(TenantService);
  protected tenantFormService = inject(TenantFormService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TenantFormGroup = this.tenantFormService.createTenantFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tenant }) => {
      this.tenant = tenant;
      if (tenant) {
        this.updateForm(tenant);
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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tenant = this.tenantFormService.getTenant(this.editForm);
    if (tenant.id !== null) {
      this.subscribeToSaveResponse(this.tenantService.update(tenant));
    } else {
      this.subscribeToSaveResponse(this.tenantService.create(tenant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITenant>>): void {
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

  protected updateForm(tenant: ITenant): void {
    this.tenant = tenant;
    this.tenantFormService.resetForm(this.editForm, tenant);
  }
}
