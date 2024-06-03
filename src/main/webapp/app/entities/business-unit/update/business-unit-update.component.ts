import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { BusinessUnitService } from '../service/business-unit.service';
import { IBusinessUnit } from '../business-unit.model';
import { BusinessUnitFormService, BusinessUnitFormGroup } from './business-unit-form.service';

@Component({
  standalone: true,
  selector: 'jhi-business-unit-update',
  templateUrl: './business-unit-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BusinessUnitUpdateComponent implements OnInit {
  isSaving = false;
  businessUnit: IBusinessUnit | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  organizationsSharedCollection: IOrganization[] = [];
  businessUnitsSharedCollection: IBusinessUnit[] = [];

  protected businessUnitService = inject(BusinessUnitService);
  protected businessUnitFormService = inject(BusinessUnitFormService);
  protected organizationService = inject(OrganizationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BusinessUnitFormGroup = this.businessUnitFormService.createBusinessUnitFormGroup();

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  compareBusinessUnit = (o1: IBusinessUnit | null, o2: IBusinessUnit | null): boolean =>
    this.businessUnitService.compareBusinessUnit(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ businessUnit }) => {
      this.businessUnit = businessUnit;
      if (businessUnit) {
        this.updateForm(businessUnit);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const businessUnit = this.businessUnitFormService.getBusinessUnit(this.editForm);
    if (businessUnit.id !== null) {
      this.subscribeToSaveResponse(this.businessUnitService.update(businessUnit));
    } else {
      this.subscribeToSaveResponse(this.businessUnitService.create(businessUnit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessUnit>>): void {
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

  protected updateForm(businessUnit: IBusinessUnit): void {
    this.businessUnit = businessUnit;
    this.businessUnitFormService.resetForm(this.editForm, businessUnit);

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      businessUnit.organization,
    );
    this.businessUnitsSharedCollection = this.businessUnitService.addBusinessUnitToCollectionIfMissing<IBusinessUnit>(
      this.businessUnitsSharedCollection,
      businessUnit.businessUnitParent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(organizations, this.businessUnit?.organization),
        ),
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));

    this.businessUnitService
      .query()
      .pipe(map((res: HttpResponse<IBusinessUnit[]>) => res.body ?? []))
      .pipe(
        map((businessUnits: IBusinessUnit[]) =>
          this.businessUnitService.addBusinessUnitToCollectionIfMissing<IBusinessUnit>(
            businessUnits,
            this.businessUnit?.businessUnitParent,
          ),
        ),
      )
      .subscribe((businessUnits: IBusinessUnit[]) => (this.businessUnitsSharedCollection = businessUnits));
  }
}
