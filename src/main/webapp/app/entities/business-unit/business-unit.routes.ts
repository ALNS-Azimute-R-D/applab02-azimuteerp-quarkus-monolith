import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BusinessUnitComponent } from './list/business-unit.component';
import { BusinessUnitDetailComponent } from './detail/business-unit-detail.component';
import { BusinessUnitUpdateComponent } from './update/business-unit-update.component';
import BusinessUnitResolve from './route/business-unit-routing-resolve.service';

const businessUnitRoute: Routes = [
  {
    path: '',
    component: BusinessUnitComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessUnitDetailComponent,
    resolve: {
      businessUnit: BusinessUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessUnitUpdateComponent,
    resolve: {
      businessUnit: BusinessUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessUnitUpdateComponent,
    resolve: {
      businessUnit: BusinessUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default businessUnitRoute;
