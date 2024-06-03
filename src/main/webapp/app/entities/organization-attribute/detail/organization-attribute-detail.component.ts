import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOrganizationAttribute } from '../organization-attribute.model';

@Component({
  standalone: true,
  selector: 'jhi-organization-attribute-detail',
  templateUrl: './organization-attribute-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrganizationAttributeDetailComponent {
  @Input() organizationAttribute: IOrganizationAttribute | null = null;

  previousState(): void {
    window.history.back();
  }
}
