import { Component, inject, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITypeOfPerson } from 'app/entities/type-of-person/type-of-person.model';
import { TypeOfPersonService } from 'app/entities/type-of-person/service/type-of-person.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { GenderEnum } from 'app/entities/enumerations/gender-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { PersonService } from '../service/person.service';
import { IPerson } from '../person.model';
import { PersonFormService, PersonFormGroup } from './person-form.service';

@Component({
  standalone: true,
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;
  person: IPerson | null = null;
  genderEnumValues = Object.keys(GenderEnum);
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  typeOfPeopleSharedCollection: ITypeOfPerson[] = [];
  districtsSharedCollection: IDistrict[] = [];
  peopleSharedCollection: IPerson[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected personService = inject(PersonService);
  protected personFormService = inject(PersonFormService);
  protected typeOfPersonService = inject(TypeOfPersonService);
  protected districtService = inject(DistrictService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PersonFormGroup = this.personFormService.createPersonFormGroup();

  compareTypeOfPerson = (o1: ITypeOfPerson | null, o2: ITypeOfPerson | null): boolean =>
    this.typeOfPersonService.compareTypeOfPerson(o1, o2);

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  comparePerson = (o1: IPerson | null, o2: IPerson | null): boolean => this.personService.comparePerson(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.person = person;
      if (person) {
        this.updateForm(person);
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
    const person = this.personFormService.getPerson(this.editForm);
    if (person.id !== null) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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

  protected updateForm(person: IPerson): void {
    this.person = person;
    this.personFormService.resetForm(this.editForm, person);

    this.typeOfPeopleSharedCollection = this.typeOfPersonService.addTypeOfPersonToCollectionIfMissing<ITypeOfPerson>(
      this.typeOfPeopleSharedCollection,
      person.typeOfPerson,
    );
    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsSharedCollection,
      person.district,
    );
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing<IPerson>(
      this.peopleSharedCollection,
      person.managerPerson,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.typeOfPersonService
      .query()
      .pipe(map((res: HttpResponse<ITypeOfPerson[]>) => res.body ?? []))
      .pipe(
        map((typeOfPeople: ITypeOfPerson[]) =>
          this.typeOfPersonService.addTypeOfPersonToCollectionIfMissing<ITypeOfPerson>(typeOfPeople, this.person?.typeOfPerson),
        ),
      )
      .subscribe((typeOfPeople: ITypeOfPerson[]) => (this.typeOfPeopleSharedCollection = typeOfPeople));

    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) => this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.person?.district)),
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing<IPerson>(people, this.person?.managerPerson)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }
}
