import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICustomerType } from '../customer-type.model';
import { CustomerTypeService } from '../service/customer-type.service';

@Component({
  standalone: true,
  templateUrl: './customer-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CustomerTypeDeleteDialogComponent {
  customerType?: ICustomerType;

  protected customerTypeService = inject(CustomerTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
