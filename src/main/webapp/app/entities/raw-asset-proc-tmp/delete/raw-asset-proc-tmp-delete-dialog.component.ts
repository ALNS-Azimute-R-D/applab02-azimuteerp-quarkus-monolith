import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRawAssetProcTmp } from '../raw-asset-proc-tmp.model';
import { RawAssetProcTmpService } from '../service/raw-asset-proc-tmp.service';

@Component({
  standalone: true,
  templateUrl: './raw-asset-proc-tmp-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RawAssetProcTmpDeleteDialogComponent {
  rawAssetProcTmp?: IRawAssetProcTmp;

  protected rawAssetProcTmpService = inject(RawAssetProcTmpService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rawAssetProcTmpService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
