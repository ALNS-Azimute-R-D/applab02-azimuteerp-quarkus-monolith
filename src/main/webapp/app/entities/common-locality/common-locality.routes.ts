import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CommonLocalityComponent } from './list/common-locality.component';
import { CommonLocalityDetailComponent } from './detail/common-locality-detail.component';
import { CommonLocalityUpdateComponent } from './update/common-locality-update.component';
import CommonLocalityResolve from './route/common-locality-routing-resolve.service';

const commonLocalityRoute: Routes = [
  {
    path: '',
    component: CommonLocalityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommonLocalityDetailComponent,
    resolve: {
      commonLocality: CommonLocalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommonLocalityUpdateComponent,
    resolve: {
      commonLocality: CommonLocalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommonLocalityUpdateComponent,
    resolve: {
      commonLocality: CommonLocalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default commonLocalityRoute;
