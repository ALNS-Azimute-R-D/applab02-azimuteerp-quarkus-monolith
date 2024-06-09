import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPaymentGateway } from '../payment-gateway.model';

@Component({
  standalone: true,
  selector: 'jhi-payment-gateway-detail',
  templateUrl: './payment-gateway-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PaymentGatewayDetailComponent {
  @Input() paymentGateway: IPaymentGateway | null = null;

  previousState(): void {
    window.history.back();
  }
}
