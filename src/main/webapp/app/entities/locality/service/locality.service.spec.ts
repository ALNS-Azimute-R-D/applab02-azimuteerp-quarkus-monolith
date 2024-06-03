import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILocality } from '../locality.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../locality.test-samples';

import { LocalityService } from './locality.service';

const requireRestSample: ILocality = {
  ...sampleWithRequiredData,
};

describe('Locality Service', () => {
  let service: LocalityService;
  let httpMock: HttpTestingController;
  let expectedResult: ILocality | ILocality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LocalityService);
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

    it('should create a Locality', () => {
      const locality = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(locality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Locality', () => {
      const locality = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(locality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Locality', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Locality', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Locality', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLocalityToCollectionIfMissing', () => {
      it('should add a Locality to an empty array', () => {
        const locality: ILocality = sampleWithRequiredData;
        expectedResult = service.addLocalityToCollectionIfMissing([], locality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(locality);
      });

      it('should not add a Locality to an array that contains it', () => {
        const locality: ILocality = sampleWithRequiredData;
        const localityCollection: ILocality[] = [
          {
            ...locality,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLocalityToCollectionIfMissing(localityCollection, locality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Locality to an array that doesn't contain it", () => {
        const locality: ILocality = sampleWithRequiredData;
        const localityCollection: ILocality[] = [sampleWithPartialData];
        expectedResult = service.addLocalityToCollectionIfMissing(localityCollection, locality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locality);
      });

      it('should add only unique Locality to an array', () => {
        const localityArray: ILocality[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const localityCollection: ILocality[] = [sampleWithRequiredData];
        expectedResult = service.addLocalityToCollectionIfMissing(localityCollection, ...localityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const locality: ILocality = sampleWithRequiredData;
        const locality2: ILocality = sampleWithPartialData;
        expectedResult = service.addLocalityToCollectionIfMissing([], locality, locality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locality);
        expect(expectedResult).toContain(locality2);
      });

      it('should accept null and undefined values', () => {
        const locality: ILocality = sampleWithRequiredData;
        expectedResult = service.addLocalityToCollectionIfMissing([], null, locality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(locality);
      });

      it('should return initial array if no Locality is added', () => {
        const localityCollection: ILocality[] = [sampleWithRequiredData];
        expectedResult = service.addLocalityToCollectionIfMissing(localityCollection, undefined, null);
        expect(expectedResult).toEqual(localityCollection);
      });
    });

    describe('compareLocality', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLocality(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLocality(entity1, entity2);
        const compareResult2 = service.compareLocality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLocality(entity1, entity2);
        const compareResult2 = service.compareLocality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLocality(entity1, entity2);
        const compareResult2 = service.compareLocality(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
