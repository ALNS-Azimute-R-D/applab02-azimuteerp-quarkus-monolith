import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDistrict } from '../district.model';
import { DistrictService } from '../service/district.service';

const districtResolve = (route: ActivatedRouteSnapshot): Observable<null | IDistrict> => {
  const id = route.params['id'];
  if (id) {
    return inject(DistrictService)
      .find(id)
      .pipe(
        mergeMap((district: HttpResponse<IDistrict>) => {
          if (district.body) {
            return of(district.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default districtResolve;
