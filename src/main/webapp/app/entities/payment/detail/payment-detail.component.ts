import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPayment } from '../payment.model';

@Component({
  standalone: true,
  selector: 'jhi-payment-detail',
  templateUrl: './payment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PaymentDetailComponent {
  @Input() payment: IPayment | null = null;

  previousState(): void {
    window.history.back();
  }
}
