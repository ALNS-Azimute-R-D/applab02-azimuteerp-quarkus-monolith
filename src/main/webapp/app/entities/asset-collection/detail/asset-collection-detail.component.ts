import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAssetCollection } from '../asset-collection.model';

@Component({
  standalone: true,
  selector: 'jhi-asset-collection-detail',
  templateUrl: './asset-collection-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AssetCollectionDetailComponent {
  @Input() assetCollection: IAssetCollection | null = null;

  previousState(): void {
    window.history.back();
  }
}
