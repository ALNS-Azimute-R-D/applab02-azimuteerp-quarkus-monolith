import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RawAssetProcTmpComponent } from './list/raw-asset-proc-tmp.component';
import { RawAssetProcTmpDetailComponent } from './detail/raw-asset-proc-tmp-detail.component';
import { RawAssetProcTmpUpdateComponent } from './update/raw-asset-proc-tmp-update.component';
import RawAssetProcTmpResolve from './route/raw-asset-proc-tmp-routing-resolve.service';

const rawAssetProcTmpRoute: Routes = [
  {
    path: '',
    component: RawAssetProcTmpComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RawAssetProcTmpDetailComponent,
    resolve: {
      rawAssetProcTmp: RawAssetProcTmpResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RawAssetProcTmpUpdateComponent,
    resolve: {
      rawAssetProcTmp: RawAssetProcTmpResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RawAssetProcTmpUpdateComponent,
    resolve: {
      rawAssetProcTmp: RawAssetProcTmpResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default rawAssetProcTmpRoute;
