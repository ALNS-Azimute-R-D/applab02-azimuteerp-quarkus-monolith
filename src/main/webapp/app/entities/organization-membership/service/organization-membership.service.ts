import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganizationMembership, NewOrganizationMembership } from '../organization-membership.model';

export type PartialUpdateOrganizationMembership = Partial<IOrganizationMembership> & Pick<IOrganizationMembership, 'id'>;

type RestOf<T extends IOrganizationMembership | NewOrganizationMembership> = Omit<T, 'joinedAt'> & {
  joinedAt?: string | null;
};

export type RestOrganizationMembership = RestOf<IOrganizationMembership>;

export type NewRestOrganizationMembership = RestOf<NewOrganizationMembership>;

export type PartialUpdateRestOrganizationMembership = RestOf<PartialUpdateOrganizationMembership>;

export type EntityResponseType = HttpResponse<IOrganizationMembership>;
export type EntityArrayResponseType = HttpResponse<IOrganizationMembership[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationMembershipService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organization-memberships');

  create(organizationMembership: NewOrganizationMembership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organizationMembership);
    return this.http
      .post<RestOrganizationMembership>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(organizationMembership: IOrganizationMembership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organizationMembership);
    return this.http
      .put<RestOrganizationMembership>(`${this.resourceUrl}/${this.getOrganizationMembershipIdentifier(organizationMembership)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(organizationMembership: PartialUpdateOrganizationMembership): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organizationMembership);
    return this.http
      .patch<RestOrganizationMembership>(`${this.resourceUrl}/${this.getOrganizationMembershipIdentifier(organizationMembership)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOrganizationMembership>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrganizationMembership[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationMembershipIdentifier(organizationMembership: Pick<IOrganizationMembership, 'id'>): number {
    return organizationMembership.id;
  }

  compareOrganizationMembership(o1: Pick<IOrganizationMembership, 'id'> | null, o2: Pick<IOrganizationMembership, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganizationMembershipIdentifier(o1) === this.getOrganizationMembershipIdentifier(o2) : o1 === o2;
  }

  addOrganizationMembershipToCollectionIfMissing<Type extends Pick<IOrganizationMembership, 'id'>>(
    organizationMembershipCollection: Type[],
    ...organizationMembershipsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organizationMemberships: Type[] = organizationMembershipsToCheck.filter(isPresent);
    if (organizationMemberships.length > 0) {
      const organizationMembershipCollectionIdentifiers = organizationMembershipCollection.map(organizationMembershipItem =>
        this.getOrganizationMembershipIdentifier(organizationMembershipItem),
      );
      const organizationMembershipsToAdd = organizationMemberships.filter(organizationMembershipItem => {
        const organizationMembershipIdentifier = this.getOrganizationMembershipIdentifier(organizationMembershipItem);
        if (organizationMembershipCollectionIdentifiers.includes(organizationMembershipIdentifier)) {
          return false;
        }
        organizationMembershipCollectionIdentifiers.push(organizationMembershipIdentifier);
        return true;
      });
      return [...organizationMembershipsToAdd, ...organizationMembershipCollection];
    }
    return organizationMembershipCollection;
  }

  protected convertDateFromClient<T extends IOrganizationMembership | NewOrganizationMembership | PartialUpdateOrganizationMembership>(
    organizationMembership: T,
  ): RestOf<T> {
    return {
      ...organizationMembership,
      joinedAt: organizationMembership.joinedAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restOrganizationMembership: RestOrganizationMembership): IOrganizationMembership {
    return {
      ...restOrganizationMembership,
      joinedAt: restOrganizationMembership.joinedAt ? dayjs(restOrganizationMembership.joinedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrganizationMembership>): HttpResponse<IOrganizationMembership> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrganizationMembership[]>): HttpResponse<IOrganizationMembership[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
