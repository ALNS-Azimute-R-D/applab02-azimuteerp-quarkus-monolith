import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeOfOrganizationComponent } from './list/type-of-organization.component';
import { TypeOfOrganizationDetailComponent } from './detail/type-of-organization-detail.component';
import { TypeOfOrganizationUpdateComponent } from './update/type-of-organization-update.component';
import TypeOfOrganizationResolve from './route/type-of-organization-routing-resolve.service';

const typeOfOrganizationRoute: Routes = [
  {
    path: '',
    component: TypeOfOrganizationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeOfOrganizationDetailComponent,
    resolve: {
      typeOfOrganization: TypeOfOrganizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeOfOrganizationUpdateComponent,
    resolve: {
      typeOfOrganization: TypeOfOrganizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeOfOrganizationUpdateComponent,
    resolve: {
      typeOfOrganization: TypeOfOrganizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeOfOrganizationRoute;
