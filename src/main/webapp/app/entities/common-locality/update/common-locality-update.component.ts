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
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { CommonLocalityService } from '../service/common-locality.service';
import { ICommonLocality } from '../common-locality.model';
import { CommonLocalityFormService, CommonLocalityFormGroup } from './common-locality-form.service';

@Component({
  standalone: true,
  selector: 'jhi-common-locality-update',
  templateUrl: './common-locality-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CommonLocalityUpdateComponent implements OnInit {
  isSaving = false;
  commonLocality: ICommonLocality | null = null;

  districtsSharedCollection: IDistrict[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected commonLocalityService = inject(CommonLocalityService);
  protected commonLocalityFormService = inject(CommonLocalityFormService);
  protected districtService = inject(DistrictService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CommonLocalityFormGroup = this.commonLocalityFormService.createCommonLocalityFormGroup();

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commonLocality }) => {
      this.commonLocality = commonLocality;
      if (commonLocality) {
        this.updateForm(commonLocality);
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
    const commonLocality = this.commonLocalityFormService.getCommonLocality(this.editForm);
    if (commonLocality.id !== null) {
      this.subscribeToSaveResponse(this.commonLocalityService.update(commonLocality));
    } else {
      this.subscribeToSaveResponse(this.commonLocalityService.create(commonLocality));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommonLocality>>): void {
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

  protected updateForm(commonLocality: ICommonLocality): void {
    this.commonLocality = commonLocality;
    this.commonLocalityFormService.resetForm(this.editForm, commonLocality);

    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsSharedCollection,
      commonLocality.district,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) =>
          this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.commonLocality?.district),
        ),
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));
  }
}
