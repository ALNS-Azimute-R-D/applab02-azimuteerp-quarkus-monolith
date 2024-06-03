import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeOfPerson } from '../type-of-person.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-of-person.test-samples';

import { TypeOfPersonService } from './type-of-person.service';

const requireRestSample: ITypeOfPerson = {
  ...sampleWithRequiredData,
};

describe('TypeOfPerson Service', () => {
  let service: TypeOfPersonService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeOfPerson | ITypeOfPerson[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeOfPersonService);
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

    it('should create a TypeOfPerson', () => {
      const typeOfPerson = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeOfPerson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeOfPerson', () => {
      const typeOfPerson = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeOfPerson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeOfPerson', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeOfPerson', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeOfPerson', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeOfPersonToCollectionIfMissing', () => {
      it('should add a TypeOfPerson to an empty array', () => {
        const typeOfPerson: ITypeOfPerson = sampleWithRequiredData;
        expectedResult = service.addTypeOfPersonToCollectionIfMissing([], typeOfPerson);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeOfPerson);
      });

      it('should not add a TypeOfPerson to an array that contains it', () => {
        const typeOfPerson: ITypeOfPerson = sampleWithRequiredData;
        const typeOfPersonCollection: ITypeOfPerson[] = [
          {
            ...typeOfPerson,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeOfPersonToCollectionIfMissing(typeOfPersonCollection, typeOfPerson);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeOfPerson to an array that doesn't contain it", () => {
        const typeOfPerson: ITypeOfPerson = sampleWithRequiredData;
        const typeOfPersonCollection: ITypeOfPerson[] = [sampleWithPartialData];
        expectedResult = service.addTypeOfPersonToCollectionIfMissing(typeOfPersonCollection, typeOfPerson);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfPerson);
      });

      it('should add only unique TypeOfPerson to an array', () => {
        const typeOfPersonArray: ITypeOfPerson[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeOfPersonCollection: ITypeOfPerson[] = [sampleWithRequiredData];
        expectedResult = service.addTypeOfPersonToCollectionIfMissing(typeOfPersonCollection, ...typeOfPersonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeOfPerson: ITypeOfPerson = sampleWithRequiredData;
        const typeOfPerson2: ITypeOfPerson = sampleWithPartialData;
        expectedResult = service.addTypeOfPersonToCollectionIfMissing([], typeOfPerson, typeOfPerson2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfPerson);
        expect(expectedResult).toContain(typeOfPerson2);
      });

      it('should accept null and undefined values', () => {
        const typeOfPerson: ITypeOfPerson = sampleWithRequiredData;
        expectedResult = service.addTypeOfPersonToCollectionIfMissing([], null, typeOfPerson, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeOfPerson);
      });

      it('should return initial array if no TypeOfPerson is added', () => {
        const typeOfPersonCollection: ITypeOfPerson[] = [sampleWithRequiredData];
        expectedResult = service.addTypeOfPersonToCollectionIfMissing(typeOfPersonCollection, undefined, null);
        expect(expectedResult).toEqual(typeOfPersonCollection);
      });
    });

    describe('compareTypeOfPerson', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeOfPerson(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeOfPerson(entity1, entity2);
        const compareResult2 = service.compareTypeOfPerson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeOfPerson(entity1, entity2);
        const compareResult2 = service.compareTypeOfPerson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeOfPerson(entity1, entity2);
        const compareResult2 = service.compareTypeOfPerson(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
