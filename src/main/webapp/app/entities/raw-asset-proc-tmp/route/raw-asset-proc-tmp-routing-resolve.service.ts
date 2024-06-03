import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRawAssetProcTmp } from '../raw-asset-proc-tmp.model';
import { RawAssetProcTmpService } from '../service/raw-asset-proc-tmp.service';

const rawAssetProcTmpResolve = (route: ActivatedRouteSnapshot): Observable<null | IRawAssetProcTmp> => {
  const id = route.params['id'];
  if (id) {
    return inject(RawAssetProcTmpService)
      .find(id)
      .pipe(
        mergeMap((rawAssetProcTmp: HttpResponse<IRawAssetProcTmp>) => {
          if (rawAssetProcTmp.body) {
            return of(rawAssetProcTmp.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default rawAssetProcTmpResolve;
