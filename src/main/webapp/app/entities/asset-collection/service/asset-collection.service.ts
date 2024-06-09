import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssetCollection, NewAssetCollection } from '../asset-collection.model';

export type PartialUpdateAssetCollection = Partial<IAssetCollection> & Pick<IAssetCollection, 'id'>;

export type EntityResponseType = HttpResponse<IAssetCollection>;
export type EntityArrayResponseType = HttpResponse<IAssetCollection[]>;

@Injectable({ providedIn: 'root' })
export class AssetCollectionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-collections');

  create(assetCollection: NewAssetCollection): Observable<EntityResponseType> {
    return this.http.post<IAssetCollection>(this.resourceUrl, assetCollection, { observe: 'response' });
  }

  update(assetCollection: IAssetCollection): Observable<EntityResponseType> {
    return this.http.put<IAssetCollection>(`${this.resourceUrl}/${this.getAssetCollectionIdentifier(assetCollection)}`, assetCollection, {
      observe: 'response',
    });
  }

  partialUpdate(assetCollection: PartialUpdateAssetCollection): Observable<EntityResponseType> {
    return this.http.patch<IAssetCollection>(`${this.resourceUrl}/${this.getAssetCollectionIdentifier(assetCollection)}`, assetCollection, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssetCollection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssetCollection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAssetCollectionIdentifier(assetCollection: Pick<IAssetCollection, 'id'>): number {
    return assetCollection.id;
  }

  compareAssetCollection(o1: Pick<IAssetCollection, 'id'> | null, o2: Pick<IAssetCollection, 'id'> | null): boolean {
    return o1 && o2 ? this.getAssetCollectionIdentifier(o1) === this.getAssetCollectionIdentifier(o2) : o1 === o2;
  }

  addAssetCollectionToCollectionIfMissing<Type extends Pick<IAssetCollection, 'id'>>(
    assetCollectionCollection: Type[],
    ...assetCollectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const assetCollections: Type[] = assetCollectionsToCheck.filter(isPresent);
    if (assetCollections.length > 0) {
      const assetCollectionCollectionIdentifiers = assetCollectionCollection.map(assetCollectionItem =>
        this.getAssetCollectionIdentifier(assetCollectionItem),
      );
      const assetCollectionsToAdd = assetCollections.filter(assetCollectionItem => {
        const assetCollectionIdentifier = this.getAssetCollectionIdentifier(assetCollectionItem);
        if (assetCollectionCollectionIdentifiers.includes(assetCollectionIdentifier)) {
          return false;
        }
        assetCollectionCollectionIdentifiers.push(assetCollectionIdentifier);
        return true;
      });
      return [...assetCollectionsToAdd, ...assetCollectionCollection];
    }
    return assetCollectionCollection;
  }
}
