import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { IOrganizationAttribute } from '../organization-attribute.model';
import { OrganizationAttributeService } from '../service/organization-attribute.service';
import { OrganizationAttributeFormService, OrganizationAttributeFormGroup } from './organization-attribute-form.service';

@Component({
  standalone: true,
  selector: 'jhi-organization-attribute-update',
  templateUrl: './organization-attribute-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrganizationAttributeUpdateComponent implements OnInit {
  isSaving = false;
  organizationAttribute: IOrganizationAttribute | null = null;

  organizationsSharedCollection: IOrganization[] = [];

  protected organizationAttributeService = inject(OrganizationAttributeService);
  protected organizationAttributeFormService = inject(OrganizationAttributeFormService);
  protected organizationService = inject(OrganizationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrganizationAttributeFormGroup = this.organizationAttributeFormService.createOrganizationAttributeFormGroup();

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organizationAttribute }) => {
      this.organizationAttribute = organizationAttribute;
      if (organizationAttribute) {
        this.updateForm(organizationAttribute);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organizationAttribute = this.organizationAttributeFormService.getOrganizationAttribute(this.editForm);
    if (organizationAttribute.id !== null) {
      this.subscribeToSaveResponse(this.organizationAttributeService.update(organizationAttribute));
    } else {
      this.subscribeToSaveResponse(this.organizationAttributeService.create(organizationAttribute));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizationAttribute>>): void {
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

  protected updateForm(organizationAttribute: IOrganizationAttribute): void {
    this.organizationAttribute = organizationAttribute;
    this.organizationAttributeFormService.resetForm(this.editForm, organizationAttribute);

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      organizationAttribute.organization,
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
            this.organizationAttribute?.organization,
          ),
        ),
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
