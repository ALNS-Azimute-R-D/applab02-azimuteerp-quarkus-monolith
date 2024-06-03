import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetType } from '../asset-type.model';
import { AssetTypeService } from '../service/asset-type.service';

const assetTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssetType> => {
  const id = route.params['id'];
  if (id) {
    return inject(AssetTypeService)
      .find(id)
      .pipe(
        mergeMap((assetType: HttpResponse<IAssetType>) => {
          if (assetType.body) {
            return of(assetType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default assetTypeResolve;
