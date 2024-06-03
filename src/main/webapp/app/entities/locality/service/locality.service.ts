import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocality, NewLocality } from '../locality.model';

export type PartialUpdateLocality = Partial<ILocality> & Pick<ILocality, 'id'>;

export type EntityResponseType = HttpResponse<ILocality>;
export type EntityArrayResponseType = HttpResponse<ILocality[]>;

@Injectable({ providedIn: 'root' })
export class LocalityService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/localities');

  create(locality: NewLocality): Observable<EntityResponseType> {
    return this.http.post<ILocality>(this.resourceUrl, locality, { observe: 'response' });
  }

  update(locality: ILocality): Observable<EntityResponseType> {
    return this.http.put<ILocality>(`${this.resourceUrl}/${this.getLocalityIdentifier(locality)}`, locality, { observe: 'response' });
  }

  partialUpdate(locality: PartialUpdateLocality): Observable<EntityResponseType> {
    return this.http.patch<ILocality>(`${this.resourceUrl}/${this.getLocalityIdentifier(locality)}`, locality, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLocalityIdentifier(locality: Pick<ILocality, 'id'>): number {
    return locality.id;
  }

  compareLocality(o1: Pick<ILocality, 'id'> | null, o2: Pick<ILocality, 'id'> | null): boolean {
    return o1 && o2 ? this.getLocalityIdentifier(o1) === this.getLocalityIdentifier(o2) : o1 === o2;
  }

  addLocalityToCollectionIfMissing<Type extends Pick<ILocality, 'id'>>(
    localityCollection: Type[],
    ...localitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const localities: Type[] = localitiesToCheck.filter(isPresent);
    if (localities.length > 0) {
      const localityCollectionIdentifiers = localityCollection.map(localityItem => this.getLocalityIdentifier(localityItem));
      const localitiesToAdd = localities.filter(localityItem => {
        const localityIdentifier = this.getLocalityIdentifier(localityItem);
        if (localityCollectionIdentifiers.includes(localityIdentifier)) {
          return false;
        }
        localityCollectionIdentifiers.push(localityIdentifier);
        return true;
      });
      return [...localitiesToAdd, ...localityCollection];
    }
    return localityCollection;
  }
}
