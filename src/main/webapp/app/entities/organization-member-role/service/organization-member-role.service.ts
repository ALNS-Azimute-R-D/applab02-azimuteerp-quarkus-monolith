import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationMemberRole, NewOrganizationMemberRole } from '../organization-member-role.model';

export type PartialUpdateOrganizationMemberRole = Partial<IOrganizationMemberRole> & Pick<IOrganizationMemberRole, 'id'>;

type RestOf<T extends IOrganizationMemberRole | NewOrganizationMemberRole> = Omit<T, 'joinedAt'> & {
  joinedAt?: string | null;
};

export type RestOrganizationMemberRole = RestOf<IOrganizationMemberRole>;

export type NewRestOrganizationMemberRole = RestOf<NewOrganizationMemberRole>;

export type PartialUpdateRestOrganizationMemberRole = RestOf<PartialUpdateOrganizationMemberRole>;

export type EntityResponseType = HttpResponse<IOrganizationMemberRole>;
export type EntityArrayResponseType = HttpResponse<IOrganizationMemberRole[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationMemberRoleService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-member-roles');

  create(organizationMemberRole: NewOrganizationMemberRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organizationMemberRole);
    return this.http
      .post<RestOrganizationMemberRole>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(organizationMemberRole: IOrganizationMemberRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organizationMemberRole);
    return this.http
      .put<RestOrganizationMemberRole>(`${this.resourceUrl}/${this.getOrganizationMemberRoleIdentifier(organizationMemberRole)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(organizationMemberRole: PartialUpdateOrganizationMemberRole): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organizationMemberRole);
    return this.http
      .patch<RestOrganizationMemberRole>(`${this.resourceUrl}/${this.getOrganizationMemberRoleIdentifier(organizationMemberRole)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOrganizationMemberRole>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrganizationMemberRole[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationMemberRoleIdentifier(organizationMemberRole: Pick<IOrganizationMemberRole, 'id'>): number {
    return organizationMemberRole.id;
  }

  compareOrganizationMemberRole(o1: Pick<IOrganizationMemberRole, 'id'> | null, o2: Pick<IOrganizationMemberRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganizationMemberRoleIdentifier(o1) === this.getOrganizationMemberRoleIdentifier(o2) : o1 === o2;
  }

  addOrganizationMemberRoleToCollectionIfMissing<Type extends Pick<IOrganizationMemberRole, 'id'>>(
    organizationMemberRoleCollection: Type[],
    ...organizationMemberRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organizationMemberRoles: Type[] = organizationMemberRolesToCheck.filter(isPresent);
    if (organizationMemberRoles.length > 0) {
      const organizationMemberRoleCollectionIdentifiers = organizationMemberRoleCollection.map(organizationMemberRoleItem =>
        this.getOrganizationMemberRoleIdentifier(organizationMemberRoleItem),
      );
      const organizationMemberRolesToAdd = organizationMemberRoles.filter(organizationMemberRoleItem => {
        const organizationMemberRoleIdentifier = this.getOrganizationMemberRoleIdentifier(organizationMemberRoleItem);
        if (organizationMemberRoleCollectionIdentifiers.includes(organizationMemberRoleIdentifier)) {
          return false;
        }
        organizationMemberRoleCollectionIdentifiers.push(organizationMemberRoleIdentifier);
        return true;
      });
      return [...organizationMemberRolesToAdd, ...organizationMemberRoleCollection];
    }
    return organizationMemberRoleCollection;
  }

  protected convertDateFromClient<T extends IOrganizationMemberRole | NewOrganizationMemberRole | PartialUpdateOrganizationMemberRole>(
    organizationMemberRole: T,
  ): RestOf<T> {
    return {
      ...organizationMemberRole,
      joinedAt: organizationMemberRole.joinedAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restOrganizationMemberRole: RestOrganizationMemberRole): IOrganizationMemberRole {
    return {
      ...restOrganizationMemberRole,
      joinedAt: restOrganizationMemberRole.joinedAt ? dayjs(restOrganizationMemberRole.joinedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrganizationMemberRole>): HttpResponse<IOrganizationMemberRole> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrganizationMemberRole[]>): HttpResponse<IOrganizationMemberRole[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
