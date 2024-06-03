import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { WarehouseComponent } from './list/warehouse.component';
import { WarehouseDetailComponent } from './detail/warehouse-detail.component';
import { WarehouseUpdateComponent } from './update/warehouse-update.component';
import WarehouseResolve from './route/warehouse-routing-resolve.service';

const warehouseRoute: Routes = [
  {
    path: '',
    component: WarehouseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WarehouseDetailComponent,
    resolve: {
      warehouse: WarehouseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WarehouseUpdateComponent,
    resolve: {
      warehouse: WarehouseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WarehouseUpdateComponent,
    resolve: {
      warehouse: WarehouseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default warehouseRoute;
