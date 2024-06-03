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
import { TypeOfOrganizationService } from '../service/type-of-organization.service';
import { ITypeOfOrganization } from '../type-of-organization.model';
import { TypeOfOrganizationFormService, TypeOfOrganizationFormGroup } from './type-of-organization-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-of-organization-update',
  templateUrl: './type-of-organization-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeOfOrganizationUpdateComponent implements OnInit {
  isSaving = false;
  typeOfOrganization: ITypeOfOrganization | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected typeOfOrganizationService = inject(TypeOfOrganizationService);
  protected typeOfOrganizationFormService = inject(TypeOfOrganizationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeOfOrganizationFormGroup = this.typeOfOrganizationFormService.createTypeOfOrganizationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeOfOrganization }) => {
      this.typeOfOrganization = typeOfOrganization;
      if (typeOfOrganization) {
        this.updateForm(typeOfOrganization);
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
    const typeOfOrganization = this.typeOfOrganizationFormService.getTypeOfOrganization(this.editForm);
    if (typeOfOrganization.id !== null) {
      this.subscribeToSaveResponse(this.typeOfOrganizationService.update(typeOfOrganization));
    } else {
      this.subscribeToSaveResponse(this.typeOfOrganizationService.create(typeOfOrganization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeOfOrganization>>): void {
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

  protected updateForm(typeOfOrganization: ITypeOfOrganization): void {
    this.typeOfOrganization = typeOfOrganization;
    this.typeOfOrganizationFormService.resetForm(this.editForm, typeOfOrganization);
  }
}
