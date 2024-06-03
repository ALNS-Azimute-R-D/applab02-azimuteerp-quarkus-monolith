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
import { ITenant } from 'app/entities/tenant/tenant.model';
import { TenantService } from 'app/entities/tenant/service/tenant.service';
import { ITypeOfOrganization } from 'app/entities/type-of-organization/type-of-organization.model';
import { TypeOfOrganizationService } from 'app/entities/type-of-organization/service/type-of-organization.service';
import { OrganizationStatusEnum } from 'app/entities/enumerations/organization-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { OrganizationService } from '../service/organization.service';
import { IOrganization } from '../organization.model';
import { OrganizationFormService, OrganizationFormGroup } from './organization-form.service';

@Component({
  standalone: true,
  selector: 'jhi-organization-update',
  templateUrl: './organization-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganizationUpdateComponent implements OnInit {
  isSaving = false;
  organization: IOrganization | null = null;
  organizationStatusEnumValues = Object.keys(OrganizationStatusEnum);
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  tenantsSharedCollection: ITenant[] = [];
  typeOfOrganizationsSharedCollection: ITypeOfOrganization[] = [];
  organizationsSharedCollection: IOrganization[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected organizationService = inject(OrganizationService);
  protected organizationFormService = inject(OrganizationFormService);
  protected tenantService = inject(TenantService);
  protected typeOfOrganizationService = inject(TypeOfOrganizationService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrganizationFormGroup = this.organizationFormService.createOrganizationFormGroup();

  compareTenant = (o1: ITenant | null, o2: ITenant | null): boolean => this.tenantService.compareTenant(o1, o2);

  compareTypeOfOrganization = (o1: ITypeOfOrganization | null, o2: ITypeOfOrganization | null): boolean =>
    this.typeOfOrganizationService.compareTypeOfOrganization(o1, o2);

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organization }) => {
      this.organization = organization;
      if (organization) {
        this.updateForm(organization);
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
    const organization = this.organizationFormService.getOrganization(this.editForm);
    if (organization.id !== null) {
      this.subscribeToSaveResponse(this.organizationService.update(organization));
    } else {
      this.subscribeToSaveResponse(this.organizationService.create(organization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganization>>): void {
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

  protected updateForm(organization: IOrganization): void {
    this.organization = organization;
    this.organizationFormService.resetForm(this.editForm, organization);

    this.tenantsSharedCollection = this.tenantService.addTenantToCollectionIfMissing<ITenant>(
      this.tenantsSharedCollection,
      organization.tenant,
    );
    this.typeOfOrganizationsSharedCollection =
      this.typeOfOrganizationService.addTypeOfOrganizationToCollectionIfMissing<ITypeOfOrganization>(
        this.typeOfOrganizationsSharedCollection,
        organization.typeOfOrganization,
      );
    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      organization.organizationParent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tenantService
      .query()
      .pipe(map((res: HttpResponse<ITenant[]>) => res.body ?? []))
      .pipe(map((tenants: ITenant[]) => this.tenantService.addTenantToCollectionIfMissing<ITenant>(tenants, this.organization?.tenant)))
      .subscribe((tenants: ITenant[]) => (this.tenantsSharedCollection = tenants));

    this.typeOfOrganizationService
      .query()
      .pipe(map((res: HttpResponse<ITypeOfOrganization[]>) => res.body ?? []))
      .pipe(
        map((typeOfOrganizations: ITypeOfOrganization[]) =>
          this.typeOfOrganizationService.addTypeOfOrganizationToCollectionIfMissing<ITypeOfOrganization>(
            typeOfOrganizations,
            this.organization?.typeOfOrganization,
          ),
        ),
      )
      .subscribe((typeOfOrganizations: ITypeOfOrganization[]) => (this.typeOfOrganizationsSharedCollection = typeOfOrganizations));

    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
            organizations,
            this.organization?.organizationParent,
          ),
        ),
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
