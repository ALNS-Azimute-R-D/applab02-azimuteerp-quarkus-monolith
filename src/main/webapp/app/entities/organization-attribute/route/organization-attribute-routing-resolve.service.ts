import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrganizationAttribute } from '../organization-attribute.model';
import { OrganizationAttributeService } from '../service/organization-attribute.service';

const organizationAttributeResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrganizationAttribute> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrganizationAttributeService)
      .find(id)
      .pipe(
        mergeMap((organizationAttribute: HttpResponse<IOrganizationAttribute>) => {
          if (organizationAttribute.body) {
            return of(organizationAttribute.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default organizationAttributeResolve;
