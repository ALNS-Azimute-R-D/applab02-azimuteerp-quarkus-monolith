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
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';
import { TownCityService } from '../service/town-city.service';
import { ITownCity } from '../town-city.model';
import { TownCityFormService, TownCityFormGroup } from './town-city-form.service';

@Component({
  standalone: true,
  selector: 'jhi-town-city-update',
  templateUrl: './town-city-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TownCityUpdateComponent implements OnInit {
  isSaving = false;
  townCity: ITownCity | null = null;

  provincesSharedCollection: IProvince[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected townCityService = inject(TownCityService);
  protected townCityFormService = inject(TownCityFormService);
  protected provinceService = inject(ProvinceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TownCityFormGroup = this.townCityFormService.createTownCityFormGroup();

  compareProvince = (o1: IProvince | null, o2: IProvince | null): boolean => this.provinceService.compareProvince(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ townCity }) => {
      this.townCity = townCity;
      if (townCity) {
        this.updateForm(townCity);
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
    const townCity = this.townCityFormService.getTownCity(this.editForm);
    if (townCity.id !== null) {
      this.subscribeToSaveResponse(this.townCityService.update(townCity));
    } else {
      this.subscribeToSaveResponse(this.townCityService.create(townCity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITownCity>>): void {
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

  protected updateForm(townCity: ITownCity): void {
    this.townCity = townCity;
    this.townCityFormService.resetForm(this.editForm, townCity);

    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing<IProvince>(
      this.provincesSharedCollection,
      townCity.province,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing<IProvince>(provinces, this.townCity?.province),
        ),
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }
}
