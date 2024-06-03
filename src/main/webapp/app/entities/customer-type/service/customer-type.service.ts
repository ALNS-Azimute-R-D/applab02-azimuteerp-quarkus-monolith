import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerType, NewCustomerType } from '../customer-type.model';

export type PartialUpdateCustomerType = Partial<ICustomerType> & Pick<ICustomerType, 'id'>;

export type EntityResponseType = HttpResponse<ICustomerType>;
export type EntityArrayResponseType = HttpResponse<ICustomerType[]>;

@Injectable({ providedIn: 'root' })
export class CustomerTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-types');

  create(customerType: NewCustomerType): Observable<EntityResponseType> {
    return this.http.post<ICustomerType>(this.resourceUrl, customerType, { observe: 'response' });
  }

  update(customerType: ICustomerType): Observable<EntityResponseType> {
    return this.http.put<ICustomerType>(`${this.resourceUrl}/${this.getCustomerTypeIdentifier(customerType)}`, customerType, {
      observe: 'response',
    });
  }

  partialUpdate(customerType: PartialUpdateCustomerType): Observable<EntityResponseType> {
    return this.http.patch<ICustomerType>(`${this.resourceUrl}/${this.getCustomerTypeIdentifier(customerType)}`, customerType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomerType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomerTypeIdentifier(customerType: Pick<ICustomerType, 'id'>): number {
    return customerType.id;
  }

  compareCustomerType(o1: Pick<ICustomerType, 'id'> | null, o2: Pick<ICustomerType, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerTypeIdentifier(o1) === this.getCustomerTypeIdentifier(o2) : o1 === o2;
  }

  addCustomerTypeToCollectionIfMissing<Type extends Pick<ICustomerType, 'id'>>(
    customerTypeCollection: Type[],
    ...customerTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customerTypes: Type[] = customerTypesToCheck.filter(isPresent);
    if (customerTypes.length > 0) {
      const customerTypeCollectionIdentifiers = customerTypeCollection.map(customerTypeItem =>
        this.getCustomerTypeIdentifier(customerTypeItem),
      );
      const customerTypesToAdd = customerTypes.filter(customerTypeItem => {
        const customerTypeIdentifier = this.getCustomerTypeIdentifier(customerTypeItem);
        if (customerTypeCollectionIdentifiers.includes(customerTypeIdentifier)) {
          return false;
        }
        customerTypeCollectionIdentifiers.push(customerTypeIdentifier);
        return true;
      });
      return [...customerTypesToAdd, ...customerTypeCollection];
    }
    return customerTypeCollection;
  }
}
