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
import { OrganizationRoleService } from '../service/organization-role.service';
import { IOrganizationRole } from '../organization-role.model';
import { OrganizationRoleFormService, OrganizationRoleFormGroup } from './organization-role-form.service';

@Component({
  standalone: true,
  selector: 'jhi-organization-role-update',
  templateUrl: './organization-role-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganizationRoleUpdateComponent implements OnInit {
  isSaving = false;
  organizationRole: IOrganizationRole | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  organizationsSharedCollection: IOrganization[] = [];

  protected organizationRoleService = inject(OrganizationRoleService);
  protected organizationRoleFormService = inject(OrganizationRoleFormService);
  protected organizationService = inject(OrganizationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrganizationRoleFormGroup = this.organizationRoleFormService.createOrganizationRoleFormGroup();

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationRole }) => {
      this.organizationRole = organizationRole;
      if (organizationRole) {
        this.updateForm(organizationRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationRole = this.organizationRoleFormService.getOrganizationRole(this.editForm);
    if (organizationRole.id !== null) {
      this.subscribeToSaveResponse(this.organizationRoleService.update(organizationRole));
    } else {
      this.subscribeToSaveResponse(this.organizationRoleService.create(organizationRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationRole>>): void {
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

  protected updateForm(organizationRole: IOrganizationRole): void {
    this.organizationRole = organizationRole;
    this.organizationRoleFormService.resetForm(this.editForm, organizationRole);

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      organizationRole.organization,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(organizations, this.organizationRole?.organization),
        ),
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
