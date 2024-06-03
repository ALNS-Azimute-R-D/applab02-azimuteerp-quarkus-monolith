import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssetMetadata, NewAssetMetadata } from '../asset-metadata.model';

export type PartialUpdateAssetMetadata = Partial<IAssetMetadata> & Pick<IAssetMetadata, 'id'>;

export type EntityResponseType = HttpResponse<IAssetMetadata>;
export type EntityArrayResponseType = HttpResponse<IAssetMetadata[]>;

@Injectable({ providedIn: 'root' })
export class AssetMetadataService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-metadata');

  create(assetMetadata: NewAssetMetadata): Observable<EntityResponseType> {
    return this.http.post<IAssetMetadata>(this.resourceUrl, assetMetadata, { observe: 'response' });
  }

  update(assetMetadata: IAssetMetadata): Observable<EntityResponseType> {
    return this.http.put<IAssetMetadata>(`${this.resourceUrl}/${this.getAssetMetadataIdentifier(assetMetadata)}`, assetMetadata, {
      observe: 'response',
    });
  }

  partialUpdate(assetMetadata: PartialUpdateAssetMetadata): Observable<EntityResponseType> {
    return this.http.patch<IAssetMetadata>(`${this.resourceUrl}/${this.getAssetMetadataIdentifier(assetMetadata)}`, assetMetadata, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssetMetadata>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssetMetadata[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAssetMetadataIdentifier(assetMetadata: Pick<IAssetMetadata, 'id'>): number {
    return assetMetadata.id;
  }

  compareAssetMetadata(o1: Pick<IAssetMetadata, 'id'> | null, o2: Pick<IAssetMetadata, 'id'> | null): boolean {
    return o1 && o2 ? this.getAssetMetadataIdentifier(o1) === this.getAssetMetadataIdentifier(o2) : o1 === o2;
  }

  addAssetMetadataToCollectionIfMissing<Type extends Pick<IAssetMetadata, 'id'>>(
    assetMetadataCollection: Type[],
    ...assetMetadataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const assetMetadata: Type[] = assetMetadataToCheck.filter(isPresent);
    if (assetMetadata.length > 0) {
      const assetMetadataCollectionIdentifiers = assetMetadataCollection.map(assetMetadataItem =>
        this.getAssetMetadataIdentifier(assetMetadataItem),
      );
      const assetMetadataToAdd = assetMetadata.filter(assetMetadataItem => {
        const assetMetadataIdentifier = this.getAssetMetadataIdentifier(assetMetadataItem);
        if (assetMetadataCollectionIdentifiers.includes(assetMetadataIdentifier)) {
          return false;
        }
        assetMetadataCollectionIdentifiers.push(assetMetadataIdentifier);
        return true;
      });
      return [...assetMetadataToAdd, ...assetMetadataCollection];
    }
    return assetMetadataCollection;
  }
}
