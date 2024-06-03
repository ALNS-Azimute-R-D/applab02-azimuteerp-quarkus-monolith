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
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { LocalityService } from '../service/locality.service';
import { ILocality } from '../locality.model';
import { LocalityFormService, LocalityFormGroup } from './locality-form.service';

@Component({
  standalone: true,
  selector: 'jhi-locality-update',
  templateUrl: './locality-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LocalityUpdateComponent implements OnInit {
  isSaving = false;
  locality: ILocality | null = null;

  countriesSharedCollection: ICountry[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected localityService = inject(LocalityService);
  protected localityFormService = inject(LocalityFormService);
  protected countryService = inject(CountryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LocalityFormGroup = this.localityFormService.createLocalityFormGroup();

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locality }) => {
      this.locality = locality;
      if (locality) {
        this.updateForm(locality);
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
    const locality = this.localityFormService.getLocality(this.editForm);
    if (locality.id !== null) {
      this.subscribeToSaveResponse(this.localityService.update(locality));
    } else {
      this.subscribeToSaveResponse(this.localityService.create(locality));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocality>>): void {
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

  protected updateForm(locality: ILocality): void {
    this.locality = locality;
    this.localityFormService.resetForm(this.editForm, locality);

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      locality.country,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.locality?.country)),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));
  }
}
