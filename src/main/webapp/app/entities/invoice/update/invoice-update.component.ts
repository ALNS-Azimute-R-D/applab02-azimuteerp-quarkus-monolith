import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPaymentGateway } from 'app/entities/payment-gateway/payment-gateway.model';
import { PaymentGatewayService } from 'app/entities/payment-gateway/service/payment-gateway.service';
import { InvoiceStatusEnum } from 'app/entities/enumerations/invoice-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
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
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  paymentGatewaysSharedCollection: IPaymentGateway[] = [];

  protected invoiceService = inject(InvoiceService);
  protected invoiceFormService = inject(InvoiceFormService);
  protected paymentGatewayService = inject(PaymentGatewayService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  comparePaymentGateway = (o1: IPaymentGateway | null, o2: IPaymentGateway | null): boolean =>
    this.paymentGatewayService.comparePaymentGateway(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }

      this.loadRelationshipsOptions();
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

    this.paymentGatewaysSharedCollection = this.paymentGatewayService.addPaymentGatewayToCollectionIfMissing<IPaymentGateway>(
      this.paymentGatewaysSharedCollection,
      invoice.preferrablePaymentGateway,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentGatewayService
      .query()
      .pipe(map((res: HttpResponse<IPaymentGateway[]>) => res.body ?? []))
      .pipe(
        map((paymentGateways: IPaymentGateway[]) =>
          this.paymentGatewayService.addPaymentGatewayToCollectionIfMissing<IPaymentGateway>(
            paymentGateways,
            this.invoice?.preferrablePaymentGateway,
          ),
        ),
      )
      .subscribe((paymentGateways: IPaymentGateway[]) => (this.paymentGatewaysSharedCollection = paymentGateways));
  }
}
