import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILocality } from '../locality.model';
import { LocalityService } from '../service/locality.service';

@Component({
  standalone: true,
  templateUrl: './locality-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LocalityDeleteDialogComponent {
  locality?: ILocality;

  protected localityService = inject(LocalityService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
