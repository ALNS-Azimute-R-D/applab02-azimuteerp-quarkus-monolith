import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRawAssetProcTmp, NewRawAssetProcTmp } from '../raw-asset-proc-tmp.model';

export type PartialUpdateRawAssetProcTmp = Partial<IRawAssetProcTmp> & Pick<IRawAssetProcTmp, 'id'>;

export type EntityResponseType = HttpResponse<IRawAssetProcTmp>;
export type EntityArrayResponseType = HttpResponse<IRawAssetProcTmp[]>;

@Injectable({ providedIn: 'root' })
export class RawAssetProcTmpService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/raw-asset-proc-tmps');

  create(rawAssetProcTmp: NewRawAssetProcTmp): Observable<EntityResponseType> {
    return this.http.post<IRawAssetProcTmp>(this.resourceUrl, rawAssetProcTmp, { observe: 'response' });
  }

  update(rawAssetProcTmp: IRawAssetProcTmp): Observable<EntityResponseType> {
    return this.http.put<IRawAssetProcTmp>(`${this.resourceUrl}/${this.getRawAssetProcTmpIdentifier(rawAssetProcTmp)}`, rawAssetProcTmp, {
      observe: 'response',
    });
  }

  partialUpdate(rawAssetProcTmp: PartialUpdateRawAssetProcTmp): Observable<EntityResponseType> {
    return this.http.patch<IRawAssetProcTmp>(`${this.resourceUrl}/${this.getRawAssetProcTmpIdentifier(rawAssetProcTmp)}`, rawAssetProcTmp, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRawAssetProcTmp>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRawAssetProcTmp[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRawAssetProcTmpIdentifier(rawAssetProcTmp: Pick<IRawAssetProcTmp, 'id'>): number {
    return rawAssetProcTmp.id;
  }

  compareRawAssetProcTmp(o1: Pick<IRawAssetProcTmp, 'id'> | null, o2: Pick<IRawAssetProcTmp, 'id'> | null): boolean {
    return o1 && o2 ? this.getRawAssetProcTmpIdentifier(o1) === this.getRawAssetProcTmpIdentifier(o2) : o1 === o2;
  }

  addRawAssetProcTmpToCollectionIfMissing<Type extends Pick<IRawAssetProcTmp, 'id'>>(
    rawAssetProcTmpCollection: Type[],
    ...rawAssetProcTmpsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rawAssetProcTmps: Type[] = rawAssetProcTmpsToCheck.filter(isPresent);
    if (rawAssetProcTmps.length > 0) {
      const rawAssetProcTmpCollectionIdentifiers = rawAssetProcTmpCollection.map(rawAssetProcTmpItem =>
        this.getRawAssetProcTmpIdentifier(rawAssetProcTmpItem),
      );
      const rawAssetProcTmpsToAdd = rawAssetProcTmps.filter(rawAssetProcTmpItem => {
        const rawAssetProcTmpIdentifier = this.getRawAssetProcTmpIdentifier(rawAssetProcTmpItem);
        if (rawAssetProcTmpCollectionIdentifiers.includes(rawAssetProcTmpIdentifier)) {
          return false;
        }
        rawAssetProcTmpCollectionIdentifiers.push(rawAssetProcTmpIdentifier);
        return true;
      });
      return [...rawAssetProcTmpsToAdd, ...rawAssetProcTmpCollection];
    }
    return rawAssetProcTmpCollection;
  }
}
