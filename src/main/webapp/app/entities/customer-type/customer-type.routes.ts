import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CustomerTypeComponent } from './list/customer-type.component';
import { CustomerTypeDetailComponent } from './detail/customer-type-detail.component';
import { CustomerTypeUpdateComponent } from './update/customer-type-update.component';
import CustomerTypeResolve from './route/customer-type-routing-resolve.service';

const customerTypeRoute: Routes = [
  {
    path: '',
    component: CustomerTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerTypeDetailComponent,
    resolve: {
      customerType: CustomerTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerTypeUpdateComponent,
    resolve: {
      customerType: CustomerTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerTypeUpdateComponent,
    resolve: {
      customerType: CustomerTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default customerTypeRoute;
