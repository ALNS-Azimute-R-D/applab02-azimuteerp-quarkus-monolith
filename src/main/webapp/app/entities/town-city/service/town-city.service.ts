import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITownCity, NewTownCity } from '../town-city.model';

export type PartialUpdateTownCity = Partial<ITownCity> & Pick<ITownCity, 'id'>;

export type EntityResponseType = HttpResponse<ITownCity>;
export type EntityArrayResponseType = HttpResponse<ITownCity[]>;

@Injectable({ providedIn: 'root' })
export class TownCityService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/town-cities');

  create(townCity: NewTownCity): Observable<EntityResponseType> {
    return this.http.post<ITownCity>(this.resourceUrl, townCity, { observe: 'response' });
  }

  update(townCity: ITownCity): Observable<EntityResponseType> {
    return this.http.put<ITownCity>(`${this.resourceUrl}/${this.getTownCityIdentifier(townCity)}`, townCity, { observe: 'response' });
  }

  partialUpdate(townCity: PartialUpdateTownCity): Observable<EntityResponseType> {
    return this.http.patch<ITownCity>(`${this.resourceUrl}/${this.getTownCityIdentifier(townCity)}`, townCity, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITownCity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITownCity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTownCityIdentifier(townCity: Pick<ITownCity, 'id'>): number {
    return townCity.id;
  }

  compareTownCity(o1: Pick<ITownCity, 'id'> | null, o2: Pick<ITownCity, 'id'> | null): boolean {
    return o1 && o2 ? this.getTownCityIdentifier(o1) === this.getTownCityIdentifier(o2) : o1 === o2;
  }

  addTownCityToCollectionIfMissing<Type extends Pick<ITownCity, 'id'>>(
    townCityCollection: Type[],
    ...townCitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const townCities: Type[] = townCitiesToCheck.filter(isPresent);
    if (townCities.length > 0) {
      const townCityCollectionIdentifiers = townCityCollection.map(townCityItem => this.getTownCityIdentifier(townCityItem));
      const townCitiesToAdd = townCities.filter(townCityItem => {
        const townCityIdentifier = this.getTownCityIdentifier(townCityItem);
        if (townCityCollectionIdentifiers.includes(townCityIdentifier)) {
          return false;
        }
        townCityCollectionIdentifiers.push(townCityIdentifier);
        return true;
      });
      return [...townCitiesToAdd, ...townCityCollection];
    }
    return townCityCollection;
  }
}
