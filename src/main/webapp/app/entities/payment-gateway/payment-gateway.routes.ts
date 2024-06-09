import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PaymentGatewayComponent } from './list/payment-gateway.component';
import { PaymentGatewayDetailComponent } from './detail/payment-gateway-detail.component';
import { PaymentGatewayUpdateComponent } from './update/payment-gateway-update.component';
import PaymentGatewayResolve from './route/payment-gateway-routing-resolve.service';

const paymentGatewayRoute: Routes = [
  {
    path: '',
    component: PaymentGatewayComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentGatewayDetailComponent,
    resolve: {
      paymentGateway: PaymentGatewayResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentGatewayUpdateComponent,
    resolve: {
      paymentGateway: PaymentGatewayResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentGatewayUpdateComponent,
    resolve: {
      paymentGateway: PaymentGatewayResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paymentGatewayRoute;
