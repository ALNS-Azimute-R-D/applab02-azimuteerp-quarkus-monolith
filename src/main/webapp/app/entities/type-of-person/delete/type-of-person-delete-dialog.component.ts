import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeOfPerson } from '../type-of-person.model';
import { TypeOfPersonService } from '../service/type-of-person.service';

@Component({
  standalone: true,
  templateUrl: './type-of-person-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeOfPersonDeleteDialogComponent {
  typeOfPerson?: ITypeOfPerson;

  protected typeOfPersonService = inject(TypeOfPersonService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeOfPersonService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
