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
import { ITownCity } from 'app/entities/town-city/town-city.model';
import { TownCityService } from 'app/entities/town-city/service/town-city.service';
import { DistrictService } from '../service/district.service';
import { IDistrict } from '../district.model';
import { DistrictFormService, DistrictFormGroup } from './district-form.service';

@Component({
  standalone: true,
  selector: 'jhi-district-update',
  templateUrl: './district-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DistrictUpdateComponent implements OnInit {
  isSaving = false;
  district: IDistrict | null = null;

  townCitiesSharedCollection: ITownCity[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected districtService = inject(DistrictService);
  protected districtFormService = inject(DistrictFormService);
  protected townCityService = inject(TownCityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DistrictFormGroup = this.districtFormService.createDistrictFormGroup();

  compareTownCity = (o1: ITownCity | null, o2: ITownCity | null): boolean => this.townCityService.compareTownCity(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ district }) => {
      this.district = district;
      if (district) {
        this.updateForm(district);
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
    const district = this.districtFormService.getDistrict(this.editForm);
    if (district.id !== null) {
      this.subscribeToSaveResponse(this.districtService.update(district));
    } else {
      this.subscribeToSaveResponse(this.districtService.create(district));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDistrict>>): void {
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

  protected updateForm(district: IDistrict): void {
    this.district = district;
    this.districtFormService.resetForm(this.editForm, district);

    this.townCitiesSharedCollection = this.townCityService.addTownCityToCollectionIfMissing<ITownCity>(
      this.townCitiesSharedCollection,
      district.townCity,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.townCityService
      .query()
      .pipe(map((res: HttpResponse<ITownCity[]>) => res.body ?? []))
      .pipe(
        map((townCities: ITownCity[]) =>
          this.townCityService.addTownCityToCollectionIfMissing<ITownCity>(townCities, this.district?.townCity),
        ),
      )
      .subscribe((townCities: ITownCity[]) => (this.townCitiesSharedCollection = townCities));
  }
}
