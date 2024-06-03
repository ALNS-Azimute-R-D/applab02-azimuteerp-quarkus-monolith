import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeOfOrganization } from '../type-of-organization.model';
import { TypeOfOrganizationService } from '../service/type-of-organization.service';

const typeOfOrganizationResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeOfOrganization> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeOfOrganizationService)
      .find(id)
      .pipe(
        mergeMap((typeOfOrganization: HttpResponse<ITypeOfOrganization>) => {
          if (typeOfOrganization.body) {
            return of(typeOfOrganization.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeOfOrganizationResolve;
