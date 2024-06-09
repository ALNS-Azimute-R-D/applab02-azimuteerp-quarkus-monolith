import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPaymentGateway } from 'app/entities/payment-gateway/payment-gateway.model';
import { PaymentGatewayService } from 'app/entities/payment-gateway/service/payment-gateway.service';
import { PaymentTypeEnum } from 'app/entities/enumerations/payment-type-enum.model';
import { PaymentStatusEnum } from 'app/entities/enumerations/payment-status-enum.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { PaymentService } from '../service/payment.service';
import { IPayment } from '../payment.model';
import { PaymentFormService, PaymentFormGroup } from './payment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  payment: IPayment | null = null;
  paymentTypeEnumValues = Object.keys(PaymentTypeEnum);
  paymentStatusEnumValues = Object.keys(PaymentStatusEnum);
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  paymentGatewaysSharedCollection: IPaymentGateway[] = [];

  protected paymentService = inject(PaymentService);
  protected paymentFormService = inject(PaymentFormService);
  protected paymentGatewayService = inject(PaymentGatewayService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentFormGroup = this.paymentFormService.createPaymentFormGroup();

  comparePaymentGateway = (o1: IPaymentGateway | null, o2: IPaymentGateway | null): boolean =>
    this.paymentGatewayService.comparePaymentGateway(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.payment = payment;
      if (payment) {
        this.updateForm(payment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.paymentFormService.getPayment(this.editForm);
    if (payment.id !== null) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  protected updateForm(payment: IPayment): void {
    this.payment = payment;
    this.paymentFormService.resetForm(this.editForm, payment);

    this.paymentGatewaysSharedCollection = this.paymentGatewayService.addPaymentGatewayToCollectionIfMissing<IPaymentGateway>(
      this.paymentGatewaysSharedCollection,
      payment.paymentGateway,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentGatewayService
      .query()
      .pipe(map((res: HttpResponse<IPaymentGateway[]>) => res.body ?? []))
      .pipe(
        map((paymentGateways: IPaymentGateway[]) =>
          this.paymentGatewayService.addPaymentGatewayToCollectionIfMissing<IPaymentGateway>(paymentGateways, this.payment?.paymentGateway),
        ),
      )
      .subscribe((paymentGateways: IPaymentGateway[]) => (this.paymentGatewaysSharedCollection = paymentGateways));
  }
}
