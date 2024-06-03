import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationDomain } from '../organization-domain.model';
import { OrganizationDomainService } from '../service/organization-domain.service';

const organizationDomainResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrganizationDomain> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrganizationDomainService)
      .find(id)
      .pipe(
        mergeMap((organizationDomain: HttpResponse<IOrganizationDomain>) => {
          if (organizationDomain.body) {
            return of(organizationDomain.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default organizationDomainResolve;
