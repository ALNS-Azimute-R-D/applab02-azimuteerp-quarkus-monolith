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
import { OrganizationDomainService } from '../service/organization-domain.service';
import { IOrganizationDomain } from '../organization-domain.model';
import { OrganizationDomainFormService, OrganizationDomainFormGroup } from './organization-domain-form.service';

@Component({
  standalone: true,
  selector: 'jhi-organization-domain-update',
  templateUrl: './organization-domain-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganizationDomainUpdateComponent implements OnInit {
  isSaving = false;
  organizationDomain: IOrganizationDomain | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  organizationsSharedCollection: IOrganization[] = [];

  protected organizationDomainService = inject(OrganizationDomainService);
  protected organizationDomainFormService = inject(OrganizationDomainFormService);
  protected organizationService = inject(OrganizationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrganizationDomainFormGroup = this.organizationDomainFormService.createOrganizationDomainFormGroup();

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationDomain }) => {
      this.organizationDomain = organizationDomain;
      if (organizationDomain) {
        this.updateForm(organizationDomain);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationDomain = this.organizationDomainFormService.getOrganizationDomain(this.editForm);
    if (organizationDomain.id !== null) {
      this.subscribeToSaveResponse(this.organizationDomainService.update(organizationDomain));
    } else {
      this.subscribeToSaveResponse(this.organizationDomainService.create(organizationDomain));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationDomain>>): void {
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

  protected updateForm(organizationDomain: IOrganizationDomain): void {
    this.organizationDomain = organizationDomain;
    this.organizationDomainFormService.resetForm(this.editForm, organizationDomain);

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      organizationDomain.organization,
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
            this.organizationDomain?.organization,
          ),
        ),
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
