import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssetType } from '../asset-type.model';
import { AssetTypeService } from '../service/asset-type.service';

@Component({
  standalone: true,
  templateUrl: './asset-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssetTypeDeleteDialogComponent {
  assetType?: IAssetType;

  protected assetTypeService = inject(AssetTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
