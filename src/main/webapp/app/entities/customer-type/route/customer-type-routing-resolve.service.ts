import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerType } from '../customer-type.model';
import { CustomerTypeService } from '../service/customer-type.service';

const customerTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustomerType> => {
  const id = route.params['id'];
  if (id) {
    return inject(CustomerTypeService)
      .find(id)
      .pipe(
        mergeMap((customerType: HttpResponse<ICustomerType>) => {
          if (customerType.body) {
            return of(customerType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default customerTypeResolve;
