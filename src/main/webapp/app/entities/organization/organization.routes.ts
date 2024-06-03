import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OrganizationComponent } from './list/organization.component';
import { OrganizationDetailComponent } from './detail/organization-detail.component';
import { OrganizationUpdateComponent } from './update/organization-update.component';
import OrganizationResolve from './route/organization-routing-resolve.service';

const organizationRoute: Routes = [
  {
    path: '',
    component: OrganizationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationDetailComponent,
    resolve: {
      organization: OrganizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationUpdateComponent,
    resolve: {
      organization: OrganizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationUpdateComponent,
    resolve: {
      organization: OrganizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default organizationRoute;
