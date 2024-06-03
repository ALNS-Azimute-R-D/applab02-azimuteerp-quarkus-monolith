import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationMemberRole } from '../organization-member-role.model';
import { OrganizationMemberRoleService } from '../service/organization-member-role.service';

const organizationMemberRoleResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrganizationMemberRole> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrganizationMemberRoleService)
      .find(id)
      .pipe(
        mergeMap((organizationMemberRole: HttpResponse<IOrganizationMemberRole>) => {
          if (organizationMemberRole.body) {
            return of(organizationMemberRole.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default organizationMemberRoleResolve;
