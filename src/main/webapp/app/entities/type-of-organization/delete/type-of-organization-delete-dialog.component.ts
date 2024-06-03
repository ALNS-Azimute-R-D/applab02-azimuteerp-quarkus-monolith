import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeOfOrganization } from '../type-of-organization.model';
import { TypeOfOrganizationService } from '../service/type-of-organization.service';

@Component({
  standalone: true,
  templateUrl: './type-of-organization-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeOfOrganizationDeleteDialogComponent {
  typeOfOrganization?: ITypeOfOrganization;

  protected typeOfOrganizationService = inject(TypeOfOrganizationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeOfOrganizationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
