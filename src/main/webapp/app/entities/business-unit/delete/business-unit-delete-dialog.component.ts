import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBusinessUnit } from '../business-unit.model';
import { BusinessUnitService } from '../service/business-unit.service';

@Component({
  standalone: true,
  templateUrl: './business-unit-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BusinessUnitDeleteDialogComponent {
  businessUnit?: IBusinessUnit;

  protected businessUnitService = inject(BusinessUnitService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessUnitService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
