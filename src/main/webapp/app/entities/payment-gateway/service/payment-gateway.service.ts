import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaymentGateway, NewPaymentGateway } from '../payment-gateway.model';

export type PartialUpdatePaymentGateway = Partial<IPaymentGateway> & Pick<IPaymentGateway, 'id'>;

export type EntityResponseType = HttpResponse<IPaymentGateway>;
export type EntityArrayResponseType = HttpResponse<IPaymentGateway[]>;

@Injectable({ providedIn: 'root' })
export class PaymentGatewayService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-gateways');

  create(paymentGateway: NewPaymentGateway): Observable<EntityResponseType> {
    return this.http.post<IPaymentGateway>(this.resourceUrl, paymentGateway, { observe: 'response' });
  }

  update(paymentGateway: IPaymentGateway): Observable<EntityResponseType> {
    return this.http.put<IPaymentGateway>(`${this.resourceUrl}/${this.getPaymentGatewayIdentifier(paymentGateway)}`, paymentGateway, {
      observe: 'response',
    });
  }

  partialUpdate(paymentGateway: PartialUpdatePaymentGateway): Observable<EntityResponseType> {
    return this.http.patch<IPaymentGateway>(`${this.resourceUrl}/${this.getPaymentGatewayIdentifier(paymentGateway)}`, paymentGateway, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentGateway>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentGateway[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaymentGatewayIdentifier(paymentGateway: Pick<IPaymentGateway, 'id'>): number {
    return paymentGateway.id;
  }

  comparePaymentGateway(o1: Pick<IPaymentGateway, 'id'> | null, o2: Pick<IPaymentGateway, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaymentGatewayIdentifier(o1) === this.getPaymentGatewayIdentifier(o2) : o1 === o2;
  }

  addPaymentGatewayToCollectionIfMissing<Type extends Pick<IPaymentGateway, 'id'>>(
    paymentGatewayCollection: Type[],
    ...paymentGatewaysToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paymentGateways: Type[] = paymentGatewaysToCheck.filter(isPresent);
    if (paymentGateways.length > 0) {
      const paymentGatewayCollectionIdentifiers = paymentGatewayCollection.map(paymentGatewayItem =>
        this.getPaymentGatewayIdentifier(paymentGatewayItem),
      );
      const paymentGatewaysToAdd = paymentGateways.filter(paymentGatewayItem => {
        const paymentGatewayIdentifier = this.getPaymentGatewayIdentifier(paymentGatewayItem);
        if (paymentGatewayCollectionIdentifiers.includes(paymentGatewayIdentifier)) {
          return false;
        }
        paymentGatewayCollectionIdentifiers.push(paymentGatewayIdentifier);
        return true;
      });
      return [...paymentGatewaysToAdd, ...paymentGatewayCollection];
    }
    return paymentGatewayCollection;
  }
}
