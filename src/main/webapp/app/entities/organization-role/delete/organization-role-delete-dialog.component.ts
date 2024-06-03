import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrganizationRole } from '../organization-role.model';
import { OrganizationRoleService } from '../service/organization-role.service';

@Component({
  standalone: true,
  templateUrl: './organization-role-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrganizationRoleDeleteDialogComponent {
  organizationRole?: IOrganizationRole;

  protected organizationRoleService = inject(OrganizationRoleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationRoleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
