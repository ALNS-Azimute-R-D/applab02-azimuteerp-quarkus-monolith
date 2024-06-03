import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetMetadata } from '../asset-metadata.model';
import { AssetMetadataService } from '../service/asset-metadata.service';

const assetMetadataResolve = (route: ActivatedRouteSnapshot): Observable<null | IAssetMetadata> => {
  const id = route.params['id'];
  if (id) {
    return inject(AssetMetadataService)
      .find(id)
      .pipe(
        mergeMap((assetMetadata: HttpResponse<IAssetMetadata>) => {
          if (assetMetadata.body) {
            return of(assetMetadata.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default assetMetadataResolve;
