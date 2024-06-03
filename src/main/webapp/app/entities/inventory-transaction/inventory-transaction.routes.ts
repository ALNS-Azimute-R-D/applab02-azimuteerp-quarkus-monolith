import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InventoryTransactionComponent } from './list/inventory-transaction.component';
import { InventoryTransactionDetailComponent } from './detail/inventory-transaction-detail.component';
import { InventoryTransactionUpdateComponent } from './update/inventory-transaction-update.component';
import InventoryTransactionResolve from './route/inventory-transaction-routing-resolve.service';

const inventoryTransactionRoute: Routes = [
  {
    path: '',
    component: InventoryTransactionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InventoryTransactionDetailComponent,
    resolve: {
      inventoryTransaction: InventoryTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InventoryTransactionUpdateComponent,
    resolve: {
      inventoryTransaction: InventoryTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InventoryTransactionUpdateComponent,
    resolve: {
      inventoryTransaction: InventoryTransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default inventoryTransactionRoute;
