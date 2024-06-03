import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IBusinessUnit } from '../business-unit.model';

@Component({
  standalone: true,
  selector: 'jhi-business-unit-detail',
  templateUrl: './business-unit-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BusinessUnitDetailComponent {
  @Input() businessUnit: IBusinessUnit | null = null;

  previousState(): void {
    window.history.back();
  }
}
