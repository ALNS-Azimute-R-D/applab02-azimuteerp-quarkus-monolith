import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DistrictComponent } from './list/district.component';
import { DistrictDetailComponent } from './detail/district-detail.component';
import { DistrictUpdateComponent } from './update/district-update.component';
import DistrictResolve from './route/district-routing-resolve.service';

const districtRoute: Routes = [
  {
    path: '',
    component: DistrictComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DistrictDetailComponent,
    resolve: {
      district: DistrictResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DistrictUpdateComponent,
    resolve: {
      district: DistrictResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DistrictUpdateComponent,
    resolve: {
      district: DistrictResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default districtRoute;
