import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProvince } from '../province.model';
import { ProvinceService } from '../service/province.service';

@Component({
  standalone: true,
  templateUrl: './province-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProvinceDeleteDialogComponent {
  province?: IProvince;

  protected provinceService = inject(ProvinceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.provinceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
