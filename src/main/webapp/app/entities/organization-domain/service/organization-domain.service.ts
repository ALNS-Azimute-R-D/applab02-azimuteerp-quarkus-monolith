import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationDomain, NewOrganizationDomain } from '../organization-domain.model';

export type PartialUpdateOrganizationDomain = Partial<IOrganizationDomain> & Pick<IOrganizationDomain, 'id'>;

export type EntityResponseType = HttpResponse<IOrganizationDomain>;
export type EntityArrayResponseType = HttpResponse<IOrganizationDomain[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationDomainService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-domains');

  create(organizationDomain: NewOrganizationDomain): Observable<EntityResponseType> {
    return this.http.post<IOrganizationDomain>(this.resourceUrl, organizationDomain, { observe: 'response' });
  }

  update(organizationDomain: IOrganizationDomain): Observable<EntityResponseType> {
    return this.http.put<IOrganizationDomain>(
      `${this.resourceUrl}/${this.getOrganizationDomainIdentifier(organizationDomain)}`,
      organizationDomain,
      { observe: 'response' },
    );
  }

  partialUpdate(organizationDomain: PartialUpdateOrganizationDomain): Observable<EntityResponseType> {
    return this.http.patch<IOrganizationDomain>(
      `${this.resourceUrl}/${this.getOrganizationDomainIdentifier(organizationDomain)}`,
      organizationDomain,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganizationDomain>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganizationDomain[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationDomainIdentifier(organizationDomain: Pick<IOrganizationDomain, 'id'>): number {
    return organizationDomain.id;
  }

  compareOrganizationDomain(o1: Pick<IOrganizationDomain, 'id'> | null, o2: Pick<IOrganizationDomain, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganizationDomainIdentifier(o1) === this.getOrganizationDomainIdentifier(o2) : o1 === o2;
  }

  addOrganizationDomainToCollectionIfMissing<Type extends Pick<IOrganizationDomain, 'id'>>(
    organizationDomainCollection: Type[],
    ...organizationDomainsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organizationDomains: Type[] = organizationDomainsToCheck.filter(isPresent);
    if (organizationDomains.length > 0) {
      const organizationDomainCollectionIdentifiers = organizationDomainCollection.map(organizationDomainItem =>
        this.getOrganizationDomainIdentifier(organizationDomainItem),
      );
      const organizationDomainsToAdd = organizationDomains.filter(organizationDomainItem => {
        const organizationDomainIdentifier = this.getOrganizationDomainIdentifier(organizationDomainItem);
        if (organizationDomainCollectionIdentifiers.includes(organizationDomainIdentifier)) {
          return false;
        }
        organizationDomainCollectionIdentifiers.push(organizationDomainIdentifier);
        return true;
      });
      return [...organizationDomainsToAdd, ...organizationDomainCollection];
    }
    return organizationDomainCollection;
  }
}
