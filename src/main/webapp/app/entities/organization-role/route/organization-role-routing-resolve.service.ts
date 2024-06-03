import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationRole } from '../organization-role.model';
import { OrganizationRoleService } from '../service/organization-role.service';

const organizationRoleResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrganizationRole> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrganizationRoleService)
      .find(id)
      .pipe(
        mergeMap((organizationRole: HttpResponse<IOrganizationRole>) => {
          if (organizationRole.body) {
            return of(organizationRole.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default organizationRoleResolve;
