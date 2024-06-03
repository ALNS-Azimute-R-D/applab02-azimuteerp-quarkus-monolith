import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeOfPersonComponent } from './list/type-of-person.component';
import { TypeOfPersonDetailComponent } from './detail/type-of-person-detail.component';
import { TypeOfPersonUpdateComponent } from './update/type-of-person-update.component';
import TypeOfPersonResolve from './route/type-of-person-routing-resolve.service';

const typeOfPersonRoute: Routes = [
  {
    path: '',
    component: TypeOfPersonComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeOfPersonDetailComponent,
    resolve: {
      typeOfPerson: TypeOfPersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeOfPersonUpdateComponent,
    resolve: {
      typeOfPerson: TypeOfPersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeOfPersonUpdateComponent,
    resolve: {
      typeOfPerson: TypeOfPersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeOfPersonRoute;
