import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OrganizationMemberRoleComponent } from './list/organization-member-role.component';
import { OrganizationMemberRoleDetailComponent } from './detail/organization-member-role-detail.component';
import { OrganizationMemberRoleUpdateComponent } from './update/organization-member-role-update.component';
import OrganizationMemberRoleResolve from './route/organization-member-role-routing-resolve.service';

const organizationMemberRoleRoute: Routes = [
  {
    path: '',
    component: OrganizationMemberRoleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationMemberRoleDetailComponent,
    resolve: {
      organizationMemberRole: OrganizationMemberRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationMemberRoleUpdateComponent,
    resolve: {
      organizationMemberRole: OrganizationMemberRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationMemberRoleUpdateComponent,
    resolve: {
      organizationMemberRole: OrganizationMemberRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default organizationMemberRoleRoute;
