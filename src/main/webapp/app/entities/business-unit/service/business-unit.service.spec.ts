import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBusinessUnit } from '../business-unit.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../business-unit.test-samples';

import { BusinessUnitService } from './business-unit.service';

const requireRestSample: IBusinessUnit = {
  ...sampleWithRequiredData,
};

describe('BusinessUnit Service', () => {
  let service: BusinessUnitService;
  let httpMock: HttpTestingController;
  let expectedResult: IBusinessUnit | IBusinessUnit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusinessUnitService);
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

    it('should create a BusinessUnit', () => {
      const businessUnit = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(businessUnit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BusinessUnit', () => {
      const businessUnit = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(businessUnit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BusinessUnit', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BusinessUnit', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BusinessUnit', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBusinessUnitToCollectionIfMissing', () => {
      it('should add a BusinessUnit to an empty array', () => {
        const businessUnit: IBusinessUnit = sampleWithRequiredData;
        expectedResult = service.addBusinessUnitToCollectionIfMissing([], businessUnit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessUnit);
      });

      it('should not add a BusinessUnit to an array that contains it', () => {
        const businessUnit: IBusinessUnit = sampleWithRequiredData;
        const businessUnitCollection: IBusinessUnit[] = [
          {
            ...businessUnit,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBusinessUnitToCollectionIfMissing(businessUnitCollection, businessUnit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BusinessUnit to an array that doesn't contain it", () => {
        const businessUnit: IBusinessUnit = sampleWithRequiredData;
        const businessUnitCollection: IBusinessUnit[] = [sampleWithPartialData];
        expectedResult = service.addBusinessUnitToCollectionIfMissing(businessUnitCollection, businessUnit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessUnit);
      });

      it('should add only unique BusinessUnit to an array', () => {
        const businessUnitArray: IBusinessUnit[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const businessUnitCollection: IBusinessUnit[] = [sampleWithRequiredData];
        expectedResult = service.addBusinessUnitToCollectionIfMissing(businessUnitCollection, ...businessUnitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const businessUnit: IBusinessUnit = sampleWithRequiredData;
        const businessUnit2: IBusinessUnit = sampleWithPartialData;
        expectedResult = service.addBusinessUnitToCollectionIfMissing([], businessUnit, businessUnit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessUnit);
        expect(expectedResult).toContain(businessUnit2);
      });

      it('should accept null and undefined values', () => {
        const businessUnit: IBusinessUnit = sampleWithRequiredData;
        expectedResult = service.addBusinessUnitToCollectionIfMissing([], null, businessUnit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessUnit);
      });

      it('should return initial array if no BusinessUnit is added', () => {
        const businessUnitCollection: IBusinessUnit[] = [sampleWithRequiredData];
        expectedResult = service.addBusinessUnitToCollectionIfMissing(businessUnitCollection, undefined, null);
        expect(expectedResult).toEqual(businessUnitCollection);
      });
    });

    describe('compareBusinessUnit', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBusinessUnit(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBusinessUnit(entity1, entity2);
        const compareResult2 = service.compareBusinessUnit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBusinessUnit(entity1, entity2);
        const compareResult2 = service.compareBusinessUnit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBusinessUnit(entity1, entity2);
        const compareResult2 = service.compareBusinessUnit(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
