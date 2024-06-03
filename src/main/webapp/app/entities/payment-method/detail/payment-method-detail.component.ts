import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPaymentMethod } from '../payment-method.model';

@Component({
  standalone: true,
  selector: 'jhi-payment-method-detail',
  templateUrl: './payment-method-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PaymentMethodDetailComponent {
  @Input() paymentMethod: IPaymentMethod | null = null;

  previousState(): void {
    window.history.back();
  }
}
