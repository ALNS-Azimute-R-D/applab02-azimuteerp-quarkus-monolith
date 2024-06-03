import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocality } from '../locality.model';
import { LocalityService } from '../service/locality.service';

const localityResolve = (route: ActivatedRouteSnapshot): Observable<null | ILocality> => {
  const id = route.params['id'];
  if (id) {
    return inject(LocalityService)
      .find(id)
      .pipe(
        mergeMap((locality: HttpResponse<ILocality>) => {
          if (locality.body) {
            return of(locality.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default localityResolve;
