import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeOfOrganization, NewTypeOfOrganization } from '../type-of-organization.model';

export type PartialUpdateTypeOfOrganization = Partial<ITypeOfOrganization> & Pick<ITypeOfOrganization, 'id'>;

export type EntityResponseType = HttpResponse<ITypeOfOrganization>;
export type EntityArrayResponseType = HttpResponse<ITypeOfOrganization[]>;

@Injectable({ providedIn: 'root' })
export class TypeOfOrganizationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-of-organizations');

  create(typeOfOrganization: NewTypeOfOrganization): Observable<EntityResponseType> {
    return this.http.post<ITypeOfOrganization>(this.resourceUrl, typeOfOrganization, { observe: 'response' });
  }

  update(typeOfOrganization: ITypeOfOrganization): Observable<EntityResponseType> {
    return this.http.put<ITypeOfOrganization>(
      `${this.resourceUrl}/${this.getTypeOfOrganizationIdentifier(typeOfOrganization)}`,
      typeOfOrganization,
      { observe: 'response' },
    );
  }

  partialUpdate(typeOfOrganization: PartialUpdateTypeOfOrganization): Observable<EntityResponseType> {
    return this.http.patch<ITypeOfOrganization>(
      `${this.resourceUrl}/${this.getTypeOfOrganizationIdentifier(typeOfOrganization)}`,
      typeOfOrganization,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeOfOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeOfOrganization[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeOfOrganizationIdentifier(typeOfOrganization: Pick<ITypeOfOrganization, 'id'>): number {
    return typeOfOrganization.id;
  }

  compareTypeOfOrganization(o1: Pick<ITypeOfOrganization, 'id'> | null, o2: Pick<ITypeOfOrganization, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeOfOrganizationIdentifier(o1) === this.getTypeOfOrganizationIdentifier(o2) : o1 === o2;
  }

  addTypeOfOrganizationToCollectionIfMissing<Type extends Pick<ITypeOfOrganization, 'id'>>(
    typeOfOrganizationCollection: Type[],
    ...typeOfOrganizationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeOfOrganizations: Type[] = typeOfOrganizationsToCheck.filter(isPresent);
    if (typeOfOrganizations.length > 0) {
      const typeOfOrganizationCollectionIdentifiers = typeOfOrganizationCollection.map(typeOfOrganizationItem =>
        this.getTypeOfOrganizationIdentifier(typeOfOrganizationItem),
      );
      const typeOfOrganizationsToAdd = typeOfOrganizations.filter(typeOfOrganizationItem => {
        const typeOfOrganizationIdentifier = this.getTypeOfOrganizationIdentifier(typeOfOrganizationItem);
        if (typeOfOrganizationCollectionIdentifiers.includes(typeOfOrganizationIdentifier)) {
          return false;
        }
        typeOfOrganizationCollectionIdentifiers.push(typeOfOrganizationIdentifier);
        return true;
      });
      return [...typeOfOrganizationsToAdd, ...typeOfOrganizationCollection];
    }
    return typeOfOrganizationCollection;
  }
}
