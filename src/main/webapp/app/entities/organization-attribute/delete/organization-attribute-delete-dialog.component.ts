import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrganizationAttribute } from '../organization-attribute.model';
import { OrganizationAttributeService } from '../service/organization-attribute.service';

@Component({
  standalone: true,
  templateUrl: './organization-attribute-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrganizationAttributeDeleteDialogComponent {
  organizationAttribute?: IOrganizationAttribute;

  protected organizationAttributeService = inject(OrganizationAttributeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.organizationAttributeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
