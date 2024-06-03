import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssetType, NewAssetType } from '../asset-type.model';

export type PartialUpdateAssetType = Partial<IAssetType> & Pick<IAssetType, 'id'>;

export type EntityResponseType = HttpResponse<IAssetType>;
export type EntityArrayResponseType = HttpResponse<IAssetType[]>;

@Injectable({ providedIn: 'root' })
export class AssetTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-types');

  create(assetType: NewAssetType): Observable<EntityResponseType> {
    return this.http.post<IAssetType>(this.resourceUrl, assetType, { observe: 'response' });
  }

  update(assetType: IAssetType): Observable<EntityResponseType> {
    return this.http.put<IAssetType>(`${this.resourceUrl}/${this.getAssetTypeIdentifier(assetType)}`, assetType, { observe: 'response' });
  }

  partialUpdate(assetType: PartialUpdateAssetType): Observable<EntityResponseType> {
    return this.http.patch<IAssetType>(`${this.resourceUrl}/${this.getAssetTypeIdentifier(assetType)}`, assetType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssetType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssetType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAssetTypeIdentifier(assetType: Pick<IAssetType, 'id'>): number {
    return assetType.id;
  }

  compareAssetType(o1: Pick<IAssetType, 'id'> | null, o2: Pick<IAssetType, 'id'> | null): boolean {
    return o1 && o2 ? this.getAssetTypeIdentifier(o1) === this.getAssetTypeIdentifier(o2) : o1 === o2;
  }

  addAssetTypeToCollectionIfMissing<Type extends Pick<IAssetType, 'id'>>(
    assetTypeCollection: Type[],
    ...assetTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const assetTypes: Type[] = assetTypesToCheck.filter(isPresent);
    if (assetTypes.length > 0) {
      const assetTypeCollectionIdentifiers = assetTypeCollection.map(assetTypeItem => this.getAssetTypeIdentifier(assetTypeItem));
      const assetTypesToAdd = assetTypes.filter(assetTypeItem => {
        const assetTypeIdentifier = this.getAssetTypeIdentifier(assetTypeItem);
        if (assetTypeCollectionIdentifiers.includes(assetTypeIdentifier)) {
          return false;
        }
        assetTypeCollectionIdentifiers.push(assetTypeIdentifier);
        return true;
      });
      return [...assetTypesToAdd, ...assetTypeCollection];
    }
    return assetTypeCollection;
  }
}
