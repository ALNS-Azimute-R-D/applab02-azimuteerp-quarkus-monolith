import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TownCityComponent } from './list/town-city.component';
import { TownCityDetailComponent } from './detail/town-city-detail.component';
import { TownCityUpdateComponent } from './update/town-city-update.component';
import TownCityResolve from './route/town-city-routing-resolve.service';

const townCityRoute: Routes = [
  {
    path: '',
    component: TownCityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TownCityDetailComponent,
    resolve: {
      townCity: TownCityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TownCityUpdateComponent,
    resolve: {
      townCity: TownCityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TownCityUpdateComponent,
    resolve: {
      townCity: TownCityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default townCityRoute;
