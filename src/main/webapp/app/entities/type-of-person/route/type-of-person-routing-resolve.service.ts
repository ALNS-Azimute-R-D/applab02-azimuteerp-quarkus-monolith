import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeOfPerson } from '../type-of-person.model';
import { TypeOfPersonService } from '../service/type-of-person.service';

const typeOfPersonResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeOfPerson> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeOfPersonService)
      .find(id)
      .pipe(
        mergeMap((typeOfPerson: HttpResponse<ITypeOfPerson>) => {
          if (typeOfPerson.body) {
            return of(typeOfPerson.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeOfPersonResolve;
