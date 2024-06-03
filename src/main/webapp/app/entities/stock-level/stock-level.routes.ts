import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StockLevelComponent } from './list/stock-level.component';
import { StockLevelDetailComponent } from './detail/stock-level-detail.component';
import { StockLevelUpdateComponent } from './update/stock-level-update.component';
import StockLevelResolve from './route/stock-level-routing-resolve.service';

const stockLevelRoute: Routes = [
  {
    path: '',
    component: StockLevelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StockLevelDetailComponent,
    resolve: {
      stockLevel: StockLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StockLevelUpdateComponent,
    resolve: {
      stockLevel: StockLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StockLevelUpdateComponent,
    resolve: {
      stockLevel: StockLevelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stockLevelRoute;
