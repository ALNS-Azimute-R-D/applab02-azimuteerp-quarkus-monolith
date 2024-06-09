import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AssetCollectionComponent } from './list/asset-collection.component';
import { AssetCollectionDetailComponent } from './detail/asset-collection-detail.component';
import { AssetCollectionUpdateComponent } from './update/asset-collection-update.component';
import AssetCollectionResolve from './route/asset-collection-routing-resolve.service';

const assetCollectionRoute: Routes = [
  {
    path: '',
    component: AssetCollectionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetCollectionDetailComponent,
    resolve: {
      assetCollection: AssetCollectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetCollectionUpdateComponent,
    resolve: {
      assetCollection: AssetCollectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetCollectionUpdateComponent,
    resolve: {
      assetCollection: AssetCollectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assetCollectionRoute;
