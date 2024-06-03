import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStockLevel } from '../stock-level.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../stock-level.test-samples';

import { StockLevelService, RestStockLevel } from './stock-level.service';

const requireRestSample: RestStockLevel = {
  ...sampleWithRequiredData,
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('StockLevel Service', () => {
  let service: StockLevelService;
  let httpMock: HttpTestingController;
  let expectedResult: IStockLevel | IStockLevel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StockLevelService);
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

    it('should create a StockLevel', () => {
      const stockLevel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stockLevel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StockLevel', () => {
      const stockLevel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stockLevel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StockLevel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StockLevel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StockLevel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStockLevelToCollectionIfMissing', () => {
      it('should add a StockLevel to an empty array', () => {
        const stockLevel: IStockLevel = sampleWithRequiredData;
        expectedResult = service.addStockLevelToCollectionIfMissing([], stockLevel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockLevel);
      });

      it('should not add a StockLevel to an array that contains it', () => {
        const stockLevel: IStockLevel = sampleWithRequiredData;
        const stockLevelCollection: IStockLevel[] = [
          {
            ...stockLevel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockLevelToCollectionIfMissing(stockLevelCollection, stockLevel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StockLevel to an array that doesn't contain it", () => {
        const stockLevel: IStockLevel = sampleWithRequiredData;
        const stockLevelCollection: IStockLevel[] = [sampleWithPartialData];
        expectedResult = service.addStockLevelToCollectionIfMissing(stockLevelCollection, stockLevel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockLevel);
      });

      it('should add only unique StockLevel to an array', () => {
        const stockLevelArray: IStockLevel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockLevelCollection: IStockLevel[] = [sampleWithRequiredData];
        expectedResult = service.addStockLevelToCollectionIfMissing(stockLevelCollection, ...stockLevelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stockLevel: IStockLevel = sampleWithRequiredData;
        const stockLevel2: IStockLevel = sampleWithPartialData;
        expectedResult = service.addStockLevelToCollectionIfMissing([], stockLevel, stockLevel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stockLevel);
        expect(expectedResult).toContain(stockLevel2);
      });

      it('should accept null and undefined values', () => {
        const stockLevel: IStockLevel = sampleWithRequiredData;
        expectedResult = service.addStockLevelToCollectionIfMissing([], null, stockLevel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stockLevel);
      });

      it('should return initial array if no StockLevel is added', () => {
        const stockLevelCollection: IStockLevel[] = [sampleWithRequiredData];
        expectedResult = service.addStockLevelToCollectionIfMissing(stockLevelCollection, undefined, null);
        expect(expectedResult).toEqual(stockLevelCollection);
      });
    });

    describe('compareStockLevel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStockLevel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStockLevel(entity1, entity2);
        const compareResult2 = service.compareStockLevel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStockLevel(entity1, entity2);
        const compareResult2 = service.compareStockLevel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStockLevel(entity1, entity2);
        const compareResult2 = service.compareStockLevel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
