import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICustomerType } from '../customer-type.model';

@Component({
  standalone: true,
  selector: 'jhi-customer-type-detail',
  templateUrl: './customer-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CustomerTypeDetailComponent {
  @Input() customerType: ICustomerType | null = null;

  previousState(): void {
    window.history.back();
  }
}
