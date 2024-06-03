import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationRole, NewOrganizationRole } from '../organization-role.model';

export type PartialUpdateOrganizationRole = Partial<IOrganizationRole> & Pick<IOrganizationRole, 'id'>;

export type EntityResponseType = HttpResponse<IOrganizationRole>;
export type EntityArrayResponseType = HttpResponse<IOrganizationRole[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationRoleService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-roles');

  create(organizationRole: NewOrganizationRole): Observable<EntityResponseType> {
    return this.http.post<IOrganizationRole>(this.resourceUrl, organizationRole, { observe: 'response' });
  }

  update(organizationRole: IOrganizationRole): Observable<EntityResponseType> {
    return this.http.put<IOrganizationRole>(
      `${this.resourceUrl}/${this.getOrganizationRoleIdentifier(organizationRole)}`,
      organizationRole,
      { observe: 'response' },
    );
  }

  partialUpdate(organizationRole: PartialUpdateOrganizationRole): Observable<EntityResponseType> {
    return this.http.patch<IOrganizationRole>(
      `${this.resourceUrl}/${this.getOrganizationRoleIdentifier(organizationRole)}`,
      organizationRole,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrganizationRole>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrganizationRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationRoleIdentifier(organizationRole: Pick<IOrganizationRole, 'id'>): number {
    return organizationRole.id;
  }

  compareOrganizationRole(o1: Pick<IOrganizationRole, 'id'> | null, o2: Pick<IOrganizationRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganizationRoleIdentifier(o1) === this.getOrganizationRoleIdentifier(o2) : o1 === o2;
  }

  addOrganizationRoleToCollectionIfMissing<Type extends Pick<IOrganizationRole, 'id'>>(
    organizationRoleCollection: Type[],
    ...organizationRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organizationRoles: Type[] = organizationRolesToCheck.filter(isPresent);
    if (organizationRoles.length > 0) {
      const organizationRoleCollectionIdentifiers = organizationRoleCollection.map(organizationRoleItem =>
        this.getOrganizationRoleIdentifier(organizationRoleItem),
      );
      const organizationRolesToAdd = organizationRoles.filter(organizationRoleItem => {
        const organizationRoleIdentifier = this.getOrganizationRoleIdentifier(organizationRoleItem);
        if (organizationRoleCollectionIdentifiers.includes(organizationRoleIdentifier)) {
          return false;
        }
        organizationRoleCollectionIdentifiers.push(organizationRoleIdentifier);
        return true;
      });
      return [...organizationRolesToAdd, ...organizationRoleCollection];
    }
    return organizationRoleCollection;
  }
}
