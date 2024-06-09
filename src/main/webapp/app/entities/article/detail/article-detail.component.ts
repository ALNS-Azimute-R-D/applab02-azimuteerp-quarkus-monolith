import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IArticle } from '../article.model';

@Component({
  standalone: true,
  selector: 'jhi-article-detail',
  templateUrl: './article-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ArticleDetailComponent {
  @Input() article: IArticle | null = null;

  previousState(): void {
    window.history.back();
  }
}
