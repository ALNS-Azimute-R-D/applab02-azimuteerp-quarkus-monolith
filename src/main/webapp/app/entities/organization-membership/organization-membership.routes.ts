import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OrganizationMembershipComponent } from './list/organization-membership.component';
import { OrganizationMembershipDetailComponent } from './detail/organization-membership-detail.component';
import { OrganizationMembershipUpdateComponent } from './update/organization-membership-update.component';
import OrganizationMembershipResolve from './route/organization-membership-routing-resolve.service';

const organizationMembershipRoute: Routes = [
  {
    path: '',
    component: OrganizationMembershipComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationMembershipDetailComponent,
    resolve: {
      organizationMembership: OrganizationMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationMembershipUpdateComponent,
    resolve: {
      organizationMembership: OrganizationMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationMembershipUpdateComponent,
    resolve: {
      organizationMembership: OrganizationMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default organizationMembershipRoute;
