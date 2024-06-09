import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssetCollection } from '../asset-collection.model';
import { AssetCollectionService } from '../service/asset-collection.service';

@Component({
  standalone: true,
  templateUrl: './asset-collection-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssetCollectionDeleteDialogComponent {
  assetCollection?: IAssetCollection;

  protected assetCollectionService = inject(AssetCollectionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetCollectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
