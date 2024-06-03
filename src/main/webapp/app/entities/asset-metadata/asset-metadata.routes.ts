import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AssetMetadataComponent } from './list/asset-metadata.component';
import { AssetMetadataDetailComponent } from './detail/asset-metadata-detail.component';
import { AssetMetadataUpdateComponent } from './update/asset-metadata-update.component';
import AssetMetadataResolve from './route/asset-metadata-routing-resolve.service';

const assetMetadataRoute: Routes = [
  {
    path: '',
    component: AssetMetadataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetMetadataDetailComponent,
    resolve: {
      assetMetadata: AssetMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetMetadataUpdateComponent,
    resolve: {
      assetMetadata: AssetMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetMetadataUpdateComponent,
    resolve: {
      assetMetadata: AssetMetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default assetMetadataRoute;
