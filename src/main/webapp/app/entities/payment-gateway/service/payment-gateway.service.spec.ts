import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaymentGateway } from '../payment-gateway.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../payment-gateway.test-samples';

import { PaymentGatewayService } from './payment-gateway.service';

const requireRestSample: IPaymentGateway = {
  ...sampleWithRequiredData,
};

describe('PaymentGateway Service', () => {
  let service: PaymentGatewayService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaymentGateway | IPaymentGateway[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentGatewayService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PaymentGateway', () => {
      const paymentGateway = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paymentGateway).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentGateway', () => {
      const paymentGateway = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paymentGateway).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentGateway', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentGateway', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaymentGateway', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPaymentGatewayToCollectionIfMissing', () => {
      it('should add a PaymentGateway to an empty array', () => {
        const paymentGateway: IPaymentGateway = sampleWithRequiredData;
        expectedResult = service.addPaymentGatewayToCollectionIfMissing([], paymentGateway);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentGateway);
      });

      it('should not add a PaymentGateway to an array that contains it', () => {
        const paymentGateway: IPaymentGateway = sampleWithRequiredData;
        const paymentGatewayCollection: IPaymentGateway[] = [
          {
            ...paymentGateway,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaymentGatewayToCollectionIfMissing(paymentGatewayCollection, paymentGateway);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentGateway to an array that doesn't contain it", () => {
        const paymentGateway: IPaymentGateway = sampleWithRequiredData;
        const paymentGatewayCollection: IPaymentGateway[] = [sampleWithPartialData];
        expectedResult = service.addPaymentGatewayToCollectionIfMissing(paymentGatewayCollection, paymentGateway);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentGateway);
      });

      it('should add only unique PaymentGateway to an array', () => {
        const paymentGatewayArray: IPaymentGateway[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paymentGatewayCollection: IPaymentGateway[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentGatewayToCollectionIfMissing(paymentGatewayCollection, ...paymentGatewayArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentGateway: IPaymentGateway = sampleWithRequiredData;
        const paymentGateway2: IPaymentGateway = sampleWithPartialData;
        expectedResult = service.addPaymentGatewayToCollectionIfMissing([], paymentGateway, paymentGateway2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentGateway);
        expect(expectedResult).toContain(paymentGateway2);
      });

      it('should accept null and undefined values', () => {
        const paymentGateway: IPaymentGateway = sampleWithRequiredData;
        expectedResult = service.addPaymentGatewayToCollectionIfMissing([], null, paymentGateway, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentGateway);
      });

      it('should return initial array if no PaymentGateway is added', () => {
        const paymentGatewayCollection: IPaymentGateway[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentGatewayToCollectionIfMissing(paymentGatewayCollection, undefined, null);
        expect(expectedResult).toEqual(paymentGatewayCollection);
      });
    });

    describe('comparePaymentGateway', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaymentGateway(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePaymentGateway(entity1, entity2);
        const compareResult2 = service.comparePaymentGateway(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePaymentGateway(entity1, entity2);
        const compareResult2 = service.comparePaymentGateway(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePaymentGateway(entity1, entity2);
        const compareResult2 = service.comparePaymentGateway(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
