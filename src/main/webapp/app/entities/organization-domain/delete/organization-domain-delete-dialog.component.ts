import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrganizationDomain } from '../organization-domain.model';
import { OrganizationDomainService } from '../service/organization-domain.service';

@Component({
  standalone: true,
  templateUrl: './organization-domain-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrganizationDomainDeleteDialogComponent {
  organizationDomain?: IOrganizationDomain;

  protected organizationDomainService = inject(OrganizationDomainService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationDomainService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
