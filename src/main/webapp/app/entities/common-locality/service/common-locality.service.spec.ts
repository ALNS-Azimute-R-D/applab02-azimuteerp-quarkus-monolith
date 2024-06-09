import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommonLocality } from '../common-locality.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../common-locality.test-samples';

import { CommonLocalityService } from './common-locality.service';

const requireRestSample: ICommonLocality = {
  ...sampleWithRequiredData,
};

describe('CommonLocality Service', () => {
  let service: CommonLocalityService;
  let httpMock: HttpTestingController;
  let expectedResult: ICommonLocality | ICommonLocality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommonLocalityService);
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

    it('should create a CommonLocality', () => {
      const commonLocality = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(commonLocality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CommonLocality', () => {
      const commonLocality = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(commonLocality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CommonLocality', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CommonLocality', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CommonLocality', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCommonLocalityToCollectionIfMissing', () => {
      it('should add a CommonLocality to an empty array', () => {
        const commonLocality: ICommonLocality = sampleWithRequiredData;
        expectedResult = service.addCommonLocalityToCollectionIfMissing([], commonLocality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commonLocality);
      });

      it('should not add a CommonLocality to an array that contains it', () => {
        const commonLocality: ICommonLocality = sampleWithRequiredData;
        const commonLocalityCollection: ICommonLocality[] = [
          {
            ...commonLocality,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCommonLocalityToCollectionIfMissing(commonLocalityCollection, commonLocality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CommonLocality to an array that doesn't contain it", () => {
        const commonLocality: ICommonLocality = sampleWithRequiredData;
        const commonLocalityCollection: ICommonLocality[] = [sampleWithPartialData];
        expectedResult = service.addCommonLocalityToCollectionIfMissing(commonLocalityCollection, commonLocality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commonLocality);
      });

      it('should add only unique CommonLocality to an array', () => {
        const commonLocalityArray: ICommonLocality[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const commonLocalityCollection: ICommonLocality[] = [sampleWithRequiredData];
        expectedResult = service.addCommonLocalityToCollectionIfMissing(commonLocalityCollection, ...commonLocalityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commonLocality: ICommonLocality = sampleWithRequiredData;
        const commonLocality2: ICommonLocality = sampleWithPartialData;
        expectedResult = service.addCommonLocalityToCollectionIfMissing([], commonLocality, commonLocality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commonLocality);
        expect(expectedResult).toContain(commonLocality2);
      });

      it('should accept null and undefined values', () => {
        const commonLocality: ICommonLocality = sampleWithRequiredData;
        expectedResult = service.addCommonLocalityToCollectionIfMissing([], null, commonLocality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commonLocality);
      });

      it('should return initial array if no CommonLocality is added', () => {
        const commonLocalityCollection: ICommonLocality[] = [sampleWithRequiredData];
        expectedResult = service.addCommonLocalityToCollectionIfMissing(commonLocalityCollection, undefined, null);
        expect(expectedResult).toEqual(commonLocalityCollection);
      });
    });

    describe('compareCommonLocality', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCommonLocality(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCommonLocality(entity1, entity2);
        const compareResult2 = service.compareCommonLocality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCommonLocality(entity1, entity2);
        const compareResult2 = service.compareCommonLocality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCommonLocality(entity1, entity2);
        const compareResult2 = service.compareCommonLocality(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
