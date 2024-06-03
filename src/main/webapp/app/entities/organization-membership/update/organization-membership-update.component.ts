import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { OrganizationMembershipService } from '../service/organization-membership.service';
import { IOrganizationMembership } from '../organization-membership.model';
import { OrganizationMembershipFormService, OrganizationMembershipFormGroup } from './organization-membership-form.service';

@Component({
  standalone: true,
  selector: 'jhi-organization-membership-update',
  templateUrl: './organization-membership-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganizationMembershipUpdateComponent implements OnInit {
  isSaving = false;
  organizationMembership: IOrganizationMembership | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  organizationsSharedCollection: IOrganization[] = [];
  peopleSharedCollection: IPerson[] = [];

  protected organizationMembershipService = inject(OrganizationMembershipService);
  protected organizationMembershipFormService = inject(OrganizationMembershipFormService);
  protected organizationService = inject(OrganizationService);
  protected personService = inject(PersonService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrganizationMembershipFormGroup = this.organizationMembershipFormService.createOrganizationMembershipFormGroup();

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  comparePerson = (o1: IPerson | null, o2: IPerson | null): boolean => this.personService.comparePerson(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationMembership }) => {
      this.organizationMembership = organizationMembership;
      if (organizationMembership) {
        this.updateForm(organizationMembership);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationMembership = this.organizationMembershipFormService.getOrganizationMembership(this.editForm);
    if (organizationMembership.id !== null) {
      this.subscribeToSaveResponse(this.organizationMembershipService.update(organizationMembership));
    } else {
      this.subscribeToSaveResponse(this.organizationMembershipService.create(organizationMembership));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationMembership>>): void {
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

  protected updateForm(organizationMembership: IOrganizationMembership): void {
    this.organizationMembership = organizationMembership;
    this.organizationMembershipFormService.resetForm(this.editForm, organizationMembership);

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      organizationMembership.organization,
    );
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing<IPerson>(
      this.peopleSharedCollection,
      organizationMembership.person,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
            organizations,
            this.organizationMembership?.organization,
          ),
        ),
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(
        map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing<IPerson>(people, this.organizationMembership?.person)),
      )
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }
}
