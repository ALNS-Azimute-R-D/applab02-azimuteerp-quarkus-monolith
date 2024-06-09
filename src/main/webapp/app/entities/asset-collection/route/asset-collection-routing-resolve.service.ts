import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetCollection } from '../asset-collection.model';
import { AssetCollectionService } from '../service/asset-collection.service';

const assetCollectionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssetCollection> => {
  const id = route.params['id'];
  if (id) {
    return inject(AssetCollectionService)
      .find(id)
      .pipe(
        mergeMap((assetCollection: HttpResponse<IAssetCollection>) => {
          if (assetCollection.body) {
            return of(assetCollection.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default assetCollectionResolve;
