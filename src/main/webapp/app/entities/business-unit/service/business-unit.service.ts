import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusinessUnit, NewBusinessUnit } from '../business-unit.model';

export type PartialUpdateBusinessUnit = Partial<IBusinessUnit> & Pick<IBusinessUnit, 'id'>;

export type EntityResponseType = HttpResponse<IBusinessUnit>;
export type EntityArrayResponseType = HttpResponse<IBusinessUnit[]>;

@Injectable({ providedIn: 'root' })
export class BusinessUnitService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/business-units');

  create(businessUnit: NewBusinessUnit): Observable<EntityResponseType> {
    return this.http.post<IBusinessUnit>(this.resourceUrl, businessUnit, { observe: 'response' });
  }

  update(businessUnit: IBusinessUnit): Observable<EntityResponseType> {
    return this.http.put<IBusinessUnit>(`${this.resourceUrl}/${this.getBusinessUnitIdentifier(businessUnit)}`, businessUnit, {
      observe: 'response',
    });
  }

  partialUpdate(businessUnit: PartialUpdateBusinessUnit): Observable<EntityResponseType> {
    return this.http.patch<IBusinessUnit>(`${this.resourceUrl}/${this.getBusinessUnitIdentifier(businessUnit)}`, businessUnit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusinessUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusinessUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBusinessUnitIdentifier(businessUnit: Pick<IBusinessUnit, 'id'>): number {
    return businessUnit.id;
  }

  compareBusinessUnit(o1: Pick<IBusinessUnit, 'id'> | null, o2: Pick<IBusinessUnit, 'id'> | null): boolean {
    return o1 && o2 ? this.getBusinessUnitIdentifier(o1) === this.getBusinessUnitIdentifier(o2) : o1 === o2;
  }

  addBusinessUnitToCollectionIfMissing<Type extends Pick<IBusinessUnit, 'id'>>(
    businessUnitCollection: Type[],
    ...businessUnitsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const businessUnits: Type[] = businessUnitsToCheck.filter(isPresent);
    if (businessUnits.length > 0) {
      const businessUnitCollectionIdentifiers = businessUnitCollection.map(businessUnitItem =>
        this.getBusinessUnitIdentifier(businessUnitItem),
      );
      const businessUnitsToAdd = businessUnits.filter(businessUnitItem => {
        const businessUnitIdentifier = this.getBusinessUnitIdentifier(businessUnitItem);
        if (businessUnitCollectionIdentifiers.includes(businessUnitIdentifier)) {
          return false;
        }
        businessUnitCollectionIdentifiers.push(businessUnitIdentifier);
        return true;
      });
      return [...businessUnitsToAdd, ...businessUnitCollection];
    }
    return businessUnitCollection;
  }
}
