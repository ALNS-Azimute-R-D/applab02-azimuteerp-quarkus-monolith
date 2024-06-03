import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOrganizationMembership } from '../organization-membership.model';

@Component({
  standalone: true,
  selector: 'jhi-organization-membership-detail',
  templateUrl: './organization-membership-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrganizationMembershipDetailComponent {
  @Input() organizationMembership: IOrganizationMembership | null = null;

  previousState(): void {
    window.history.back();
  }
}
