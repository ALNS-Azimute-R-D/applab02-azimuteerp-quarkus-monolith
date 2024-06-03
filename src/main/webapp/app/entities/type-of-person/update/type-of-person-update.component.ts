import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeOfPerson } from '../type-of-person.model';
import { TypeOfPersonService } from '../service/type-of-person.service';
import { TypeOfPersonFormService, TypeOfPersonFormGroup } from './type-of-person-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-of-person-update',
  templateUrl: './type-of-person-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeOfPersonUpdateComponent implements OnInit {
  isSaving = false;
  typeOfPerson: ITypeOfPerson | null = null;

  protected typeOfPersonService = inject(TypeOfPersonService);
  protected typeOfPersonFormService = inject(TypeOfPersonFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeOfPersonFormGroup = this.typeOfPersonFormService.createTypeOfPersonFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeOfPerson }) => {
      this.typeOfPerson = typeOfPerson;
      if (typeOfPerson) {
        this.updateForm(typeOfPerson);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeOfPerson = this.typeOfPersonFormService.getTypeOfPerson(this.editForm);
    if (typeOfPerson.id !== null) {
      this.subscribeToSaveResponse(this.typeOfPersonService.update(typeOfPerson));
    } else {
      this.subscribeToSaveResponse(this.typeOfPersonService.create(typeOfPerson));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeOfPerson>>): void {
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

  protected updateForm(typeOfPerson: ITypeOfPerson): void {
    this.typeOfPerson = typeOfPerson;
    this.typeOfPersonFormService.resetForm(this.editForm, typeOfPerson);
  }
}
