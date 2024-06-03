import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWarehouse } from '../warehouse.model';
import { WarehouseService } from '../service/warehouse.service';

const warehouseResolve = (route: ActivatedRouteSnapshot): Observable<null | IWarehouse> => {
  const id = route.params['id'];
  if (id) {
    return inject(WarehouseService)
      .find(id)
      .pipe(
        mergeMap((warehouse: HttpResponse<IWarehouse>) => {
          if (warehouse.body) {
            return of(warehouse.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default warehouseResolve;
