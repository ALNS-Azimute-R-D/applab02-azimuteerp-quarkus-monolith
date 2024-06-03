import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessUnit } from '../business-unit.model';
import { BusinessUnitService } from '../service/business-unit.service';

const businessUnitResolve = (route: ActivatedRouteSnapshot): Observable<null | IBusinessUnit> => {
  const id = route.params['id'];
  if (id) {
    return inject(BusinessUnitService)
      .find(id)
      .pipe(
        mergeMap((businessUnit: HttpResponse<IBusinessUnit>) => {
          if (businessUnit.body) {
            return of(businessUnit.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default businessUnitResolve;
