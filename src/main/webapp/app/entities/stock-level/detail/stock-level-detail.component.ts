import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IStockLevel } from '../stock-level.model';

@Component({
  standalone: true,
  selector: 'jhi-stock-level-detail',
  templateUrl: './stock-level-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StockLevelDetailComponent {
  @Input() stockLevel: IStockLevel | null = null;

  previousState(): void {
    window.history.back();
  }
}
