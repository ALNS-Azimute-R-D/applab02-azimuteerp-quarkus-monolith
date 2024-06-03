import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAssetMetadata } from '../asset-metadata.model';
import { AssetMetadataService } from '../service/asset-metadata.service';

@Component({
  standalone: true,
  templateUrl: './asset-metadata-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AssetMetadataDeleteDialogComponent {
  assetMetadata?: IAssetMetadata;

  protected assetMetadataService = inject(AssetMetadataService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetMetadataService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
