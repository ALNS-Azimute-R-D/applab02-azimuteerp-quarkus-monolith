import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AssetTypeComponent } from './list/asset-type.component';
import { AssetTypeDetailComponent } from './detail/asset-type-detail.component';
import { AssetTypeUpdateComponent } from './update/asset-type-update.component';
import AssetTypeResolve from './route/asset-type-routing-resolve.service';

const assetTypeRoute: Routes = [
  {
    path: '',
    component: AssetTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetTypeDetailComponent,
    resolve: {
      assetType: AssetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetTypeUpdateComponent,
    resolve: {
      assetType: AssetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetTypeUpdateComponent,
    resolve: {
      assetType: AssetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assetTypeRoute;
