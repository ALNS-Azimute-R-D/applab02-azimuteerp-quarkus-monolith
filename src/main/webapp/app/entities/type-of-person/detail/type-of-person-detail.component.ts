import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITypeOfPerson } from '../type-of-person.model';

@Component({
  standalone: true,
  selector: 'jhi-type-of-person-detail',
  templateUrl: './type-of-person-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TypeOfPersonDetailComponent {
  @Input() typeOfPerson: ITypeOfPerson | null = null;

  previousState(): void {
    window.history.back();
  }
}
