import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAssetMetadata } from '../asset-metadata.model';

@Component({
  standalone: true,
  selector: 'jhi-asset-metadata-detail',
  templateUrl: './asset-metadata-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AssetMetadataDetailComponent {
  @Input() assetMetadata: IAssetMetadata | null = null;

  previousState(): void {
    window.history.back();
  }
}
