import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommonLocality, NewCommonLocality } from '../common-locality.model';

export type PartialUpdateCommonLocality = Partial<ICommonLocality> & Pick<ICommonLocality, 'id'>;

export type EntityResponseType = HttpResponse<ICommonLocality>;
export type EntityArrayResponseType = HttpResponse<ICommonLocality[]>;

@Injectable({ providedIn: 'root' })
export class CommonLocalityService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/common-localities');

  create(commonLocality: NewCommonLocality): Observable<EntityResponseType> {
    return this.http.post<ICommonLocality>(this.resourceUrl, commonLocality, { observe: 'response' });
  }

  update(commonLocality: ICommonLocality): Observable<EntityResponseType> {
    return this.http.put<ICommonLocality>(`${this.resourceUrl}/${this.getCommonLocalityIdentifier(commonLocality)}`, commonLocality, {
      observe: 'response',
    });
  }

  partialUpdate(commonLocality: PartialUpdateCommonLocality): Observable<EntityResponseType> {
    return this.http.patch<ICommonLocality>(`${this.resourceUrl}/${this.getCommonLocalityIdentifier(commonLocality)}`, commonLocality, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommonLocality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommonLocality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommonLocalityIdentifier(commonLocality: Pick<ICommonLocality, 'id'>): number {
    return commonLocality.id;
  }

  compareCommonLocality(o1: Pick<ICommonLocality, 'id'> | null, o2: Pick<ICommonLocality, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommonLocalityIdentifier(o1) === this.getCommonLocalityIdentifier(o2) : o1 === o2;
  }

  addCommonLocalityToCollectionIfMissing<Type extends Pick<ICommonLocality, 'id'>>(
    commonLocalityCollection: Type[],
    ...commonLocalitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const commonLocalities: Type[] = commonLocalitiesToCheck.filter(isPresent);
    if (commonLocalities.length > 0) {
      const commonLocalityCollectionIdentifiers = commonLocalityCollection.map(commonLocalityItem =>
        this.getCommonLocalityIdentifier(commonLocalityItem),
      );
      const commonLocalitiesToAdd = commonLocalities.filter(commonLocalityItem => {
        const commonLocalityIdentifier = this.getCommonLocalityIdentifier(commonLocalityItem);
        if (commonLocalityCollectionIdentifiers.includes(commonLocalityIdentifier)) {
          return false;
        }
        commonLocalityCollectionIdentifiers.push(commonLocalityIdentifier);
        return true;
      });
      return [...commonLocalitiesToAdd, ...commonLocalityCollection];
    }
    return commonLocalityCollection;
  }
}
