import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOrganizationMemberRole } from '../organization-member-role.model';

@Component({
  standalone: true,
  selector: 'jhi-organization-member-role-detail',
  templateUrl: './organization-member-role-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrganizationMemberRoleDetailComponent {
  @Input() organizationMemberRole: IOrganizationMemberRole | null = null;

  previousState(): void {
    window.history.back();
  }
}
