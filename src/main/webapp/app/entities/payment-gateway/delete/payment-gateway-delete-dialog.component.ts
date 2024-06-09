import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPaymentGateway } from '../payment-gateway.model';
import { PaymentGatewayService } from '../service/payment-gateway.service';

@Component({
  standalone: true,
  templateUrl: './payment-gateway-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PaymentGatewayDeleteDialogComponent {
  paymentGateway?: IPaymentGateway;

  protected paymentGatewayService = inject(PaymentGatewayService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentGatewayService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
