import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWarehouse } from '../warehouse.model';
import { WarehouseService } from '../service/warehouse.service';

@Component({
  standalone: true,
  templateUrl: './warehouse-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WarehouseDeleteDialogComponent {
  warehouse?: IWarehouse;

  protected warehouseService = inject(WarehouseService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.warehouseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
