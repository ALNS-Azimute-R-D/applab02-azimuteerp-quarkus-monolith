import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITypeOfOrganization } from '../type-of-organization.model';

@Component({
  standalone: true,
  selector: 'jhi-type-of-organization-detail',
  templateUrl: './type-of-organization-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TypeOfOrganizationDetailComponent {
  @Input() typeOfOrganization: ITypeOfOrganization | null = null;

  previousState(): void {
    window.history.back();
  }
}
