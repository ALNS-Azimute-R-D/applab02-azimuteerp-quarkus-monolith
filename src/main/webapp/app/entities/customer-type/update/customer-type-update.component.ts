import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomerType } from '../customer-type.model';
import { CustomerTypeService } from '../service/customer-type.service';
import { CustomerTypeFormService, CustomerTypeFormGroup } from './customer-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-customer-type-update',
  templateUrl: './customer-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomerTypeUpdateComponent implements OnInit {
  isSaving = false;
  customerType: ICustomerType | null = null;

  protected customerTypeService = inject(CustomerTypeService);
  protected customerTypeFormService = inject(CustomerTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomerTypeFormGroup = this.customerTypeFormService.createCustomerTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerType }) => {
      this.customerType = customerType;
      if (customerType) {
        this.updateForm(customerType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerType = this.customerTypeFormService.getCustomerType(this.editForm);
    if (customerType.id !== null) {
      this.subscribeToSaveResponse(this.customerTypeService.update(customerType));
    } else {
      this.subscribeToSaveResponse(this.customerTypeService.create(customerType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerType>>): void {
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

  protected updateForm(customerType: ICustomerType): void {
    this.customerType = customerType;
    this.customerTypeFormService.resetForm(this.editForm, customerType);
  }
}
