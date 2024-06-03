import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationAttribute, NewOrganizationAttribute } from '../organization-attribute.model';

export type PartialUpdateOrganizationAttribute = Partial<IOrganizationAttribute> & Pick<IOrganizationAttribute, 'id'>;

export type EntityResponseType = HttpResponse<IOrganizationAttribute>;
export type EntityArrayResponseType = HttpResponse<IOrganizationAttribute[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationAttributeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-attributes');

  create(organizationAttribute: NewOrganizationAttribute): Observable<EntityResponseType> {
    return this.http.post<IOrganizationAttribute>(this.resourceUrl, organizationAttribute, { observe: 'response' });
  }

  update(organizationAttribute: IOrganizationAttribute): Observable<EntityResponseType> {
    return this.http.put<IOrganizationAttribute>(
      `${this.resourceUrl}/${this.getOrganizationAttributeIdentifier(organizationAttribute)}`,
      organizationAttribute,
      { observe: 'response' },
    );
  }

  partialUpdate(organizationAttribute: PartialUpdateOrganizationAttribute): Observable<EntityResponseType> {
    return this.http.patch<IOrganizationAttribute>(
      `${this.resourceUrl}/${this.getOrganizationAttributeIdentifier(organizationAttribute)}`,
      organizationAttribute,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganizationAttribute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganizationAttribute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationAttributeIdentifier(organizationAttribute: Pick<IOrganizationAttribute, 'id'>): number {
    return organizationAttribute.id;
  }

  compareOrganizationAttribute(o1: Pick<IOrganizationAttribute, 'id'> | null, o2: Pick<IOrganizationAttribute, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganizationAttributeIdentifier(o1) === this.getOrganizationAttributeIdentifier(o2) : o1 === o2;
  }

  addOrganizationAttributeToCollectionIfMissing<Type extends Pick<IOrganizationAttribute, 'id'>>(
    organizationAttributeCollection: Type[],
    ...organizationAttributesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organizationAttributes: Type[] = organizationAttributesToCheck.filter(isPresent);
    if (organizationAttributes.length > 0) {
      const organizationAttributeCollectionIdentifiers = organizationAttributeCollection.map(organizationAttributeItem =>
        this.getOrganizationAttributeIdentifier(organizationAttributeItem),
      );
      const organizationAttributesToAdd = organizationAttributes.filter(organizationAttributeItem => {
        const organizationAttributeIdentifier = this.getOrganizationAttributeIdentifier(organizationAttributeItem);
        if (organizationAttributeCollectionIdentifiers.includes(organizationAttributeIdentifier)) {
          return false;
        }
        organizationAttributeCollectionIdentifiers.push(organizationAttributeIdentifier);
        return true;
      });
      return [...organizationAttributesToAdd, ...organizationAttributeCollection];
    }
    return organizationAttributeCollection;
  }
}
