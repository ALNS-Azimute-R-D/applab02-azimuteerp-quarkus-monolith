import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrganizationMembership } from 'app/entities/organization-membership/organization-membership.model';
import { OrganizationMembershipService } from 'app/entities/organization-membership/service/organization-membership.service';
import { IOrganizationRole } from 'app/entities/organization-role/organization-role.model';
import { OrganizationRoleService } from 'app/entities/organization-role/service/organization-role.service';
import { OrganizationMemberRoleService } from '../service/organization-member-role.service';
import { IOrganizationMemberRole } from '../organization-member-role.model';
import { OrganizationMemberRoleFormService, OrganizationMemberRoleFormGroup } from './organization-member-role-form.service';

@Component({
  standalone: true,
  selector: 'jhi-organization-member-role-update',
  templateUrl: './organization-member-role-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganizationMemberRoleUpdateComponent implements OnInit {
  isSaving = false;
  organizationMemberRole: IOrganizationMemberRole | null = null;

  organizationMembershipsSharedCollection: IOrganizationMembership[] = [];
  organizationRolesSharedCollection: IOrganizationRole[] = [];

  protected organizationMemberRoleService = inject(OrganizationMemberRoleService);
  protected organizationMemberRoleFormService = inject(OrganizationMemberRoleFormService);
  protected organizationMembershipService = inject(OrganizationMembershipService);
  protected organizationRoleService = inject(OrganizationRoleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrganizationMemberRoleFormGroup = this.organizationMemberRoleFormService.createOrganizationMemberRoleFormGroup();

  compareOrganizationMembership = (o1: IOrganizationMembership | null, o2: IOrganizationMembership | null): boolean =>
    this.organizationMembershipService.compareOrganizationMembership(o1, o2);

  compareOrganizationRole = (o1: IOrganizationRole | null, o2: IOrganizationRole | null): boolean =>
    this.organizationRoleService.compareOrganizationRole(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationMemberRole }) => {
      this.organizationMemberRole = organizationMemberRole;
      if (organizationMemberRole) {
        this.updateForm(organizationMemberRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationMemberRole = this.organizationMemberRoleFormService.getOrganizationMemberRole(this.editForm);
    if (organizationMemberRole.id !== null) {
      this.subscribeToSaveResponse(this.organizationMemberRoleService.update(organizationMemberRole));
    } else {
      this.subscribeToSaveResponse(this.organizationMemberRoleService.create(organizationMemberRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationMemberRole>>): void {
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

  protected updateForm(organizationMemberRole: IOrganizationMemberRole): void {
    this.organizationMemberRole = organizationMemberRole;
    this.organizationMemberRoleFormService.resetForm(this.editForm, organizationMemberRole);

    this.organizationMembershipsSharedCollection =
      this.organizationMembershipService.addOrganizationMembershipToCollectionIfMissing<IOrganizationMembership>(
        this.organizationMembershipsSharedCollection,
        organizationMemberRole.organizationMembership,
      );
    this.organizationRolesSharedCollection = this.organizationRoleService.addOrganizationRoleToCollectionIfMissing<IOrganizationRole>(
      this.organizationRolesSharedCollection,
      organizationMemberRole.organizationRole,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organizationMembershipService
      .query()
      .pipe(map((res: HttpResponse<IOrganizationMembership[]>) => res.body ?? []))
      .pipe(
        map((organizationMemberships: IOrganizationMembership[]) =>
          this.organizationMembershipService.addOrganizationMembershipToCollectionIfMissing<IOrganizationMembership>(
            organizationMemberships,
            this.organizationMemberRole?.organizationMembership,
          ),
        ),
      )
      .subscribe(
        (organizationMemberships: IOrganizationMembership[]) => (this.organizationMembershipsSharedCollection = organizationMemberships),
      );

    this.organizationRoleService
      .query()
      .pipe(map((res: HttpResponse<IOrganizationRole[]>) => res.body ?? []))
      .pipe(
        map((organizationRoles: IOrganizationRole[]) =>
          this.organizationRoleService.addOrganizationRoleToCollectionIfMissing<IOrganizationRole>(
            organizationRoles,
            this.organizationMemberRole?.organizationRole,
          ),
        ),
      )
      .subscribe((organizationRoles: IOrganizationRole[]) => (this.organizationRolesSharedCollection = organizationRoles));
  }
}
