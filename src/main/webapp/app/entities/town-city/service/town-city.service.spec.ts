import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITownCity } from '../town-city.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../town-city.test-samples';

import { TownCityService } from './town-city.service';

const requireRestSample: ITownCity = {
  ...sampleWithRequiredData,
};

describe('TownCity Service', () => {
  let service: TownCityService;
  let httpMock: HttpTestingController;
  let expectedResult: ITownCity | ITownCity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TownCityService);
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

    it('should create a TownCity', () => {
      const townCity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(townCity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TownCity', () => {
      const townCity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(townCity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TownCity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TownCity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TownCity', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTownCityToCollectionIfMissing', () => {
      it('should add a TownCity to an empty array', () => {
        const townCity: ITownCity = sampleWithRequiredData;
        expectedResult = service.addTownCityToCollectionIfMissing([], townCity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(townCity);
      });

      it('should not add a TownCity to an array that contains it', () => {
        const townCity: ITownCity = sampleWithRequiredData;
        const townCityCollection: ITownCity[] = [
          {
            ...townCity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTownCityToCollectionIfMissing(townCityCollection, townCity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TownCity to an array that doesn't contain it", () => {
        const townCity: ITownCity = sampleWithRequiredData;
        const townCityCollection: ITownCity[] = [sampleWithPartialData];
        expectedResult = service.addTownCityToCollectionIfMissing(townCityCollection, townCity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(townCity);
      });

      it('should add only unique TownCity to an array', () => {
        const townCityArray: ITownCity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const townCityCollection: ITownCity[] = [sampleWithRequiredData];
        expectedResult = service.addTownCityToCollectionIfMissing(townCityCollection, ...townCityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const townCity: ITownCity = sampleWithRequiredData;
        const townCity2: ITownCity = sampleWithPartialData;
        expectedResult = service.addTownCityToCollectionIfMissing([], townCity, townCity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(townCity);
        expect(expectedResult).toContain(townCity2);
      });

      it('should accept null and undefined values', () => {
        const townCity: ITownCity = sampleWithRequiredData;
        expectedResult = service.addTownCityToCollectionIfMissing([], null, townCity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(townCity);
      });

      it('should return initial array if no TownCity is added', () => {
        const townCityCollection: ITownCity[] = [sampleWithRequiredData];
        expectedResult = service.addTownCityToCollectionIfMissing(townCityCollection, undefined, null);
        expect(expectedResult).toEqual(townCityCollection);
      });
    });

    describe('compareTownCity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTownCity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTownCity(entity1, entity2);
        const compareResult2 = service.compareTownCity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTownCity(entity1, entity2);
        const compareResult2 = service.compareTownCity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTownCity(entity1, entity2);
        const compareResult2 = service.compareTownCity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
