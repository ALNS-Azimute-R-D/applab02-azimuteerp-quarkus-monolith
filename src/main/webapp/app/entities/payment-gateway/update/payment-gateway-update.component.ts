import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { IPaymentGateway } from '../payment-gateway.model';
import { PaymentGatewayService } from '../service/payment-gateway.service';
import { PaymentGatewayFormService, PaymentGatewayFormGroup } from './payment-gateway-form.service';

@Component({
  standalone: true,
  selector: 'jhi-payment-gateway-update',
  templateUrl: './payment-gateway-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaymentGatewayUpdateComponent implements OnInit {
  isSaving = false;
  paymentGateway: IPaymentGateway | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  protected paymentGatewayService = inject(PaymentGatewayService);
  protected paymentGatewayFormService = inject(PaymentGatewayFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentGatewayFormGroup = this.paymentGatewayFormService.createPaymentGatewayFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentGateway }) => {
      this.paymentGateway = paymentGateway;
      if (paymentGateway) {
        this.updateForm(paymentGateway);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentGateway = this.paymentGatewayFormService.getPaymentGateway(this.editForm);
    if (paymentGateway.id !== null) {
      this.subscribeToSaveResponse(this.paymentGatewayService.update(paymentGateway));
    } else {
      this.subscribeToSaveResponse(this.paymentGatewayService.create(paymentGateway));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentGateway>>): void {
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

  protected updateForm(paymentGateway: IPaymentGateway): void {
    this.paymentGateway = paymentGateway;
    this.paymentGatewayFormService.resetForm(this.editForm, paymentGateway);
  }
}
