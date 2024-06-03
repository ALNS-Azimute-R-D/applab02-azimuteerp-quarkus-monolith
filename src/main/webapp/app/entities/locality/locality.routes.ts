import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LocalityComponent } from './list/locality.component';
import { LocalityDetailComponent } from './detail/locality-detail.component';
import { LocalityUpdateComponent } from './update/locality-update.component';
import LocalityResolve from './route/locality-routing-resolve.service';

const localityRoute: Routes = [
  {
    path: '',
    component: LocalityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocalityDetailComponent,
    resolve: {
      locality: LocalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocalityUpdateComponent,
    resolve: {
      locality: LocalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocalityUpdateComponent,
    resolve: {
      locality: LocalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default localityRoute;
