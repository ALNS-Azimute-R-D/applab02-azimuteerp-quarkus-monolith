import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { InvoiceStatusEnum } from 'app/entities/enumerations/invoice-status-enum.model';
import { InvoiceService } from '../service/invoice.service';
import { IInvoice } from '../invoice.model';
import { InvoiceFormService, InvoiceFormGroup } from './invoice-form.service';

@Component({
  standalone: true,
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  invoice: IInvoice | null = null;
  invoiceStatusEnumValues = Object.keys(InvoiceStatusEnum);

  paymentMethodsSharedCollection: IPaymentMethod[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected invoiceService = inject(InvoiceService);
  protected invoiceFormService = inject(InvoiceFormService);
  protected paymentMethodService = inject(PaymentMethodService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  comparePaymentMethod = (o1: IPaymentMethod | null, o2: IPaymentMethod | null): boolean =>
    this.paymentMethodService.comparePaymentMethod(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.paymentMethodsSharedCollection = this.paymentMethodService.addPaymentMethodToCollectionIfMissing<IPaymentMethod>(
      this.paymentMethodsSharedCollection,
      invoice.preferrablePaymentMethod,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentMethodService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethod[]>) => res.body ?? []))
      .pipe(
        map((paymentMethods: IPaymentMethod[]) =>
          this.paymentMethodService.addPaymentMethodToCollectionIfMissing<IPaymentMethod>(
            paymentMethods,
            this.invoice?.preferrablePaymentMethod,
          ),
        ),
      )
      .subscribe((paymentMethods: IPaymentMethod[]) => (this.paymentMethodsSharedCollection = paymentMethods));
  }
}
