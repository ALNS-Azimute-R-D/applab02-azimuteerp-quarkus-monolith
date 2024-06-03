import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWarehouse, NewWarehouse } from '../warehouse.model';

export type PartialUpdateWarehouse = Partial<IWarehouse> & Pick<IWarehouse, 'id'>;

export type EntityResponseType = HttpResponse<IWarehouse>;
export type EntityArrayResponseType = HttpResponse<IWarehouse[]>;

@Injectable({ providedIn: 'root' })
export class WarehouseService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/warehouses');

  create(warehouse: NewWarehouse): Observable<EntityResponseType> {
    return this.http.post<IWarehouse>(this.resourceUrl, warehouse, { observe: 'response' });
  }

  update(warehouse: IWarehouse): Observable<EntityResponseType> {
    return this.http.put<IWarehouse>(`${this.resourceUrl}/${this.getWarehouseIdentifier(warehouse)}`, warehouse, { observe: 'response' });
  }

  partialUpdate(warehouse: PartialUpdateWarehouse): Observable<EntityResponseType> {
    return this.http.patch<IWarehouse>(`${this.resourceUrl}/${this.getWarehouseIdentifier(warehouse)}`, warehouse, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWarehouse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWarehouse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWarehouseIdentifier(warehouse: Pick<IWarehouse, 'id'>): number {
    return warehouse.id;
  }

  compareWarehouse(o1: Pick<IWarehouse, 'id'> | null, o2: Pick<IWarehouse, 'id'> | null): boolean {
    return o1 && o2 ? this.getWarehouseIdentifier(o1) === this.getWarehouseIdentifier(o2) : o1 === o2;
  }

  addWarehouseToCollectionIfMissing<Type extends Pick<IWarehouse, 'id'>>(
    warehouseCollection: Type[],
    ...warehousesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const warehouses: Type[] = warehousesToCheck.filter(isPresent);
    if (warehouses.length > 0) {
      const warehouseCollectionIdentifiers = warehouseCollection.map(warehouseItem => this.getWarehouseIdentifier(warehouseItem));
      const warehousesToAdd = warehouses.filter(warehouseItem => {
        const warehouseIdentifier = this.getWarehouseIdentifier(warehouseItem);
        if (warehouseCollectionIdentifiers.includes(warehouseIdentifier)) {
          return false;
        }
        warehouseCollectionIdentifiers.push(warehouseIdentifier);
        return true;
      });
      return [...warehousesToAdd, ...warehouseCollection];
    }
    return warehouseCollection;
  }
}
