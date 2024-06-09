import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommonLocality } from '../common-locality.model';
import { CommonLocalityService } from '../service/common-locality.service';

const commonLocalityResolve = (route: ActivatedRouteSnapshot): Observable<null | ICommonLocality> => {
  const id = route.params['id'];
  if (id) {
    return inject(CommonLocalityService)
      .find(id)
      .pipe(
        mergeMap((commonLocality: HttpResponse<ICommonLocality>) => {
          if (commonLocality.body) {
            return of(commonLocality.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default commonLocalityResolve;
