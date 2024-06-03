import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOrganizationRole } from '../organization-role.model';

@Component({
  standalone: true,
  selector: 'jhi-organization-role-detail',
  templateUrl: './organization-role-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrganizationRoleDetailComponent {
  @Input() organizationRole: IOrganizationRole | null = null;

  previousState(): void {
    window.history.back();
  }
}
