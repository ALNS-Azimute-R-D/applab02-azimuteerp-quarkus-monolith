import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeOfPerson, NewTypeOfPerson } from '../type-of-person.model';

export type PartialUpdateTypeOfPerson = Partial<ITypeOfPerson> & Pick<ITypeOfPerson, 'id'>;

export type EntityResponseType = HttpResponse<ITypeOfPerson>;
export type EntityArrayResponseType = HttpResponse<ITypeOfPerson[]>;

@Injectable({ providedIn: 'root' })
export class TypeOfPersonService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-of-people');

  create(typeOfPerson: NewTypeOfPerson): Observable<EntityResponseType> {
    return this.http.post<ITypeOfPerson>(this.resourceUrl, typeOfPerson, { observe: 'response' });
  }

  update(typeOfPerson: ITypeOfPerson): Observable<EntityResponseType> {
    return this.http.put<ITypeOfPerson>(`${this.resourceUrl}/${this.getTypeOfPersonIdentifier(typeOfPerson)}`, typeOfPerson, {
      observe: 'response',
    });
  }

  partialUpdate(typeOfPerson: PartialUpdateTypeOfPerson): Observable<EntityResponseType> {
    return this.http.patch<ITypeOfPerson>(`${this.resourceUrl}/${this.getTypeOfPersonIdentifier(typeOfPerson)}`, typeOfPerson, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeOfPerson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeOfPerson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeOfPersonIdentifier(typeOfPerson: Pick<ITypeOfPerson, 'id'>): number {
    return typeOfPerson.id;
  }

  compareTypeOfPerson(o1: Pick<ITypeOfPerson, 'id'> | null, o2: Pick<ITypeOfPerson, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeOfPersonIdentifier(o1) === this.getTypeOfPersonIdentifier(o2) : o1 === o2;
  }

  addTypeOfPersonToCollectionIfMissing<Type extends Pick<ITypeOfPerson, 'id'>>(
    typeOfPersonCollection: Type[],
    ...typeOfPeopleToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeOfPeople: Type[] = typeOfPeopleToCheck.filter(isPresent);
    if (typeOfPeople.length > 0) {
      const typeOfPersonCollectionIdentifiers = typeOfPersonCollection.map(typeOfPersonItem =>
        this.getTypeOfPersonIdentifier(typeOfPersonItem),
      );
      const typeOfPeopleToAdd = typeOfPeople.filter(typeOfPersonItem => {
        const typeOfPersonIdentifier = this.getTypeOfPersonIdentifier(typeOfPersonItem);
        if (typeOfPersonCollectionIdentifiers.includes(typeOfPersonIdentifier)) {
          return false;
        }
        typeOfPersonCollectionIdentifiers.push(typeOfPersonIdentifier);
        return true;
      });
      return [...typeOfPeopleToAdd, ...typeOfPersonCollection];
    }
    return typeOfPersonCollection;
  }
}
