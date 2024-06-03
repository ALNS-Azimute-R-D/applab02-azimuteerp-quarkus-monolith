import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICustomerType } from '../customer-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../customer-type.test-samples';

import { CustomerTypeService } from './customer-type.service';

const requireRestSample: ICustomerType = {
  ...sampleWithRequiredData,
};

describe('CustomerType Service', () => {
  let service: CustomerTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomerType | ICustomerType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerTypeService);
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

    it('should create a CustomerType', () => {
      const customerType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customerType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerType', () => {
      const customerType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customerType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomerType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomerTypeToCollectionIfMissing', () => {
      it('should add a CustomerType to an empty array', () => {
        const customerType: ICustomerType = sampleWithRequiredData;
        expectedResult = service.addCustomerTypeToCollectionIfMissing([], customerType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerType);
      });

      it('should not add a CustomerType to an array that contains it', () => {
        const customerType: ICustomerType = sampleWithRequiredData;
        const customerTypeCollection: ICustomerType[] = [
          {
            ...customerType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomerTypeToCollectionIfMissing(customerTypeCollection, customerType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerType to an array that doesn't contain it", () => {
        const customerType: ICustomerType = sampleWithRequiredData;
        const customerTypeCollection: ICustomerType[] = [sampleWithPartialData];
        expectedResult = service.addCustomerTypeToCollectionIfMissing(customerTypeCollection, customerType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerType);
      });

      it('should add only unique CustomerType to an array', () => {
        const customerTypeArray: ICustomerType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customerTypeCollection: ICustomerType[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerTypeToCollectionIfMissing(customerTypeCollection, ...customerTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerType: ICustomerType = sampleWithRequiredData;
        const customerType2: ICustomerType = sampleWithPartialData;
        expectedResult = service.addCustomerTypeToCollectionIfMissing([], customerType, customerType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerType);
        expect(expectedResult).toContain(customerType2);
      });

      it('should accept null and undefined values', () => {
        const customerType: ICustomerType = sampleWithRequiredData;
        expectedResult = service.addCustomerTypeToCollectionIfMissing([], null, customerType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerType);
      });

      it('should return initial array if no CustomerType is added', () => {
        const customerTypeCollection: ICustomerType[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerTypeToCollectionIfMissing(customerTypeCollection, undefined, null);
        expect(expectedResult).toEqual(customerTypeCollection);
      });
    });

    describe('compareCustomerType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomerType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCustomerType(entity1, entity2);
        const compareResult2 = service.compareCustomerType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCustomerType(entity1, entity2);
        const compareResult2 = service.compareCustomerType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCustomerType(entity1, entity2);
        const compareResult2 = service.compareCustomerType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
