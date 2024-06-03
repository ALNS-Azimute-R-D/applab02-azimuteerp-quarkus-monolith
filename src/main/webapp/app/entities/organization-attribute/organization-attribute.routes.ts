import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OrganizationAttributeComponent } from './list/organization-attribute.component';
import { OrganizationAttributeDetailComponent } from './detail/organization-attribute-detail.component';
import { OrganizationAttributeUpdateComponent } from './update/organization-attribute-update.component';
import OrganizationAttributeResolve from './route/organization-attribute-routing-resolve.service';

const organizationAttributeRoute: Routes = [
  {
    path: '',
    component: OrganizationAttributeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrganizationAttributeDetailComponent,
    resolve: {
      organizationAttribute: OrganizationAttributeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrganizationAttributeUpdateComponent,
    resolve: {
      organizationAttribute: OrganizationAttributeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrganizationAttributeUpdateComponent,
    resolve: {
      organizationAttribute: OrganizationAttributeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default organizationAttributeRoute;
