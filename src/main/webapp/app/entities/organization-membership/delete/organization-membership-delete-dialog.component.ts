import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrganizationMembership } from '../organization-membership.model';
import { OrganizationMembershipService } from '../service/organization-membership.service';

@Component({
  standalone: true,
  templateUrl: './organization-membership-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrganizationMembershipDeleteDialogComponent {
  organizationMembership?: IOrganizationMembership;

  protected organizationMembershipService = inject(OrganizationMembershipService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationMembershipService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
