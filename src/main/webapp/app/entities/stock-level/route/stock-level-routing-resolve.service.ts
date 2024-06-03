import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStockLevel } from '../stock-level.model';
import { StockLevelService } from '../service/stock-level.service';

const stockLevelResolve = (route: ActivatedRouteSnapshot): Observable<null | IStockLevel> => {
  const id = route.params['id'];
  if (id) {
    return inject(StockLevelService)
      .find(id)
      .pipe(
        mergeMap((stockLevel: HttpResponse<IStockLevel>) => {
          if (stockLevel.body) {
            return of(stockLevel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default stockLevelResolve;
