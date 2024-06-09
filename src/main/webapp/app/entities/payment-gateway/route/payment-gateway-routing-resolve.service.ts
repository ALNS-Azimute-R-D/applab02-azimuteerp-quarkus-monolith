import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentGateway } from '../payment-gateway.model';
import { PaymentGatewayService } from '../service/payment-gateway.service';

const paymentGatewayResolve = (route: ActivatedRouteSnapshot): Observable<null | IPaymentGateway> => {
  const id = route.params['id'];
  if (id) {
    return inject(PaymentGatewayService)
      .find(id)
      .pipe(
        mergeMap((paymentGateway: HttpResponse<IPaymentGateway>) => {
          if (paymentGateway.body) {
            return of(paymentGateway.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default paymentGatewayResolve;
