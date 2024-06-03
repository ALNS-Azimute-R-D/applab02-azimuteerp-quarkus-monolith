import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStockLevel, NewStockLevel } from '../stock-level.model';

export type PartialUpdateStockLevel = Partial<IStockLevel> & Pick<IStockLevel, 'id'>;

type RestOf<T extends IStockLevel | NewStockLevel> = Omit<T, 'lastModifiedDate'> & {
  lastModifiedDate?: string | null;
};

export type RestStockLevel = RestOf<IStockLevel>;

export type NewRestStockLevel = RestOf<NewStockLevel>;

export type PartialUpdateRestStockLevel = RestOf<PartialUpdateStockLevel>;

export type EntityResponseType = HttpResponse<IStockLevel>;
export type EntityArrayResponseType = HttpResponse<IStockLevel[]>;

@Injectable({ providedIn: 'root' })
export class StockLevelService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stock-levels');

  create(stockLevel: NewStockLevel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockLevel);
    return this.http
      .post<RestStockLevel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(stockLevel: IStockLevel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockLevel);
    return this.http
      .put<RestStockLevel>(`${this.resourceUrl}/${this.getStockLevelIdentifier(stockLevel)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(stockLevel: PartialUpdateStockLevel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockLevel);
    return this.http
      .patch<RestStockLevel>(`${this.resourceUrl}/${this.getStockLevelIdentifier(stockLevel)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestStockLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestStockLevel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStockLevelIdentifier(stockLevel: Pick<IStockLevel, 'id'>): number {
    return stockLevel.id;
  }

  compareStockLevel(o1: Pick<IStockLevel, 'id'> | null, o2: Pick<IStockLevel, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockLevelIdentifier(o1) === this.getStockLevelIdentifier(o2) : o1 === o2;
  }

  addStockLevelToCollectionIfMissing<Type extends Pick<IStockLevel, 'id'>>(
    stockLevelCollection: Type[],
    ...stockLevelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockLevels: Type[] = stockLevelsToCheck.filter(isPresent);
    if (stockLevels.length > 0) {
      const stockLevelCollectionIdentifiers = stockLevelCollection.map(stockLevelItem => this.getStockLevelIdentifier(stockLevelItem));
      const stockLevelsToAdd = stockLevels.filter(stockLevelItem => {
        const stockLevelIdentifier = this.getStockLevelIdentifier(stockLevelItem);
        if (stockLevelCollectionIdentifiers.includes(stockLevelIdentifier)) {
          return false;
        }
        stockLevelCollectionIdentifiers.push(stockLevelIdentifier);
        return true;
      });
      return [...stockLevelsToAdd, ...stockLevelCollection];
    }
    return stockLevelCollection;
  }

  protected convertDateFromClient<T extends IStockLevel | NewStockLevel | PartialUpdateStockLevel>(stockLevel: T): RestOf<T> {
    return {
      ...stockLevel,
      lastModifiedDate: stockLevel.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restStockLevel: RestStockLevel): IStockLevel {
    return {
      ...restStockLevel,
      lastModifiedDate: restStockLevel.lastModifiedDate ? dayjs(restStockLevel.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestStockLevel>): HttpResponse<IStockLevel> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestStockLevel[]>): HttpResponse<IStockLevel[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
