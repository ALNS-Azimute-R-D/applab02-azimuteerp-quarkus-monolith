import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAssetType } from '../asset-type.model';

@Component({
  standalone: true,
  selector: 'jhi-asset-type-detail',
  templateUrl: './asset-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AssetTypeDetailComponent {
  @Input() assetType: IAssetType | null = null;

  previousState(): void {
    window.history.back();
  }
}
