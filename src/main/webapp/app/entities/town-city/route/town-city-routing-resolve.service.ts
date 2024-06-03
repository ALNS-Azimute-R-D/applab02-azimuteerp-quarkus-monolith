import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITownCity } from '../town-city.model';
import { TownCityService } from '../service/town-city.service';

const townCityResolve = (route: ActivatedRouteSnapshot): Observable<null | ITownCity> => {
  const id = route.params['id'];
  if (id) {
    return inject(TownCityService)
      .find(id)
      .pipe(
        mergeMap((townCity: HttpResponse<ITownCity>) => {
          if (townCity.body) {
            return of(townCity.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default townCityResolve;
