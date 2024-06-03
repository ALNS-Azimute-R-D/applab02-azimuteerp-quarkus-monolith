import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OrganizationRoleComponent } from './list/organization-role.component';
import { OrganizationRoleDetailComponent } from './detail/organization-role-detail.component';
import { OrganizationRoleUpdateComponent } from './update/organization-role-update.component';
import OrganizationRoleResolve from './route/organization-role-routing-resolve.service';

const organizationRoleRoute: Routes = [
  {
    path: '',
    component: OrganizationRoleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationRoleDetailComponent,
    resolve: {
      organizationRole: OrganizationRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationRoleUpdateComponent,
    resolve: {
      organizationRole: OrganizationRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationRoleUpdateComponent,
    resolve: {
      organizationRole: OrganizationRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default organizationRoleRoute;
