import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationMembership } from '../organization-membership.model';
import { OrganizationMembershipService } from '../service/organization-membership.service';

const organizationMembershipResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrganizationMembership> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrganizationMembershipService)
      .find(id)
      .pipe(
        mergeMap((organizationMembership: HttpResponse<IOrganizationMembership>) => {
          if (organizationMembership.body) {
            return of(organizationMembership.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default organizationMembershipResolve;
