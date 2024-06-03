import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDistrict } from '../district.model';
import { DistrictService } from '../service/district.service';

@Component({
  standalone: true,
  templateUrl: './district-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DistrictDeleteDialogComponent {
  district?: IDistrict;

  protected districtService = inject(DistrictService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.districtService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
