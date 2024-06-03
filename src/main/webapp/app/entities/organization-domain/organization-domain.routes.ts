import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OrganizationDomainComponent } from './list/organization-domain.component';
import { OrganizationDomainDetailComponent } from './detail/organization-domain-detail.component';
import { OrganizationDomainUpdateComponent } from './update/organization-domain-update.component';
import OrganizationDomainResolve from './route/organization-domain-routing-resolve.service';

const organizationDomainRoute: Routes = [
  {
    path: '',
    component: OrganizationDomainComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationDomainDetailComponent,
    resolve: {
      organizationDomain: OrganizationDomainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationDomainUpdateComponent,
    resolve: {
      organizationDomain: OrganizationDomainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationDomainUpdateComponent,
    resolve: {
      organizationDomain: OrganizationDomainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default organizationDomainRoute;
