import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeOfOrganization } from '../type-of-organization.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-of-organization.test-samples';

import { TypeOfOrganizationService } from './type-of-organization.service';

const requireRestSample: ITypeOfOrganization = {
  ...sampleWithRequiredData,
};

describe('TypeOfOrganization Service', () => {
  let service: TypeOfOrganizationService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeOfOrganization | ITypeOfOrganization[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeOfOrganizationService);
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

    it('should create a TypeOfOrganization', () => {
      const typeOfOrganization = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeOfOrganization).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeOfOrganization', () => {
      const typeOfOrganization = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeOfOrganization).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeOfOrganization', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeOfOrganization', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeOfOrganization', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeOfOrganizationToCollectionIfMissing', () => {
      it('should add a TypeOfOrganization to an empty array', () => {
        const typeOfOrganization: ITypeOfOrganization = sampleWithRequiredData;
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing([], typeOfOrganization);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeOfOrganization);
      });

      it('should not add a TypeOfOrganization to an array that contains it', () => {
        const typeOfOrganization: ITypeOfOrganization = sampleWithRequiredData;
        const typeOfOrganizationCollection: ITypeOfOrganization[] = [
          {
            ...typeOfOrganization,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing(typeOfOrganizationCollection, typeOfOrganization);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeOfOrganization to an array that doesn't contain it", () => {
        const typeOfOrganization: ITypeOfOrganization = sampleWithRequiredData;
        const typeOfOrganizationCollection: ITypeOfOrganization[] = [sampleWithPartialData];
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing(typeOfOrganizationCollection, typeOfOrganization);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfOrganization);
      });

      it('should add only unique TypeOfOrganization to an array', () => {
        const typeOfOrganizationArray: ITypeOfOrganization[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeOfOrganizationCollection: ITypeOfOrganization[] = [sampleWithRequiredData];
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing(typeOfOrganizationCollection, ...typeOfOrganizationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeOfOrganization: ITypeOfOrganization = sampleWithRequiredData;
        const typeOfOrganization2: ITypeOfOrganization = sampleWithPartialData;
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing([], typeOfOrganization, typeOfOrganization2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfOrganization);
        expect(expectedResult).toContain(typeOfOrganization2);
      });

      it('should accept null and undefined values', () => {
        const typeOfOrganization: ITypeOfOrganization = sampleWithRequiredData;
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing([], null, typeOfOrganization, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeOfOrganization);
      });

      it('should return initial array if no TypeOfOrganization is added', () => {
        const typeOfOrganizationCollection: ITypeOfOrganization[] = [sampleWithRequiredData];
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing(typeOfOrganizationCollection, undefined, null);
        expect(expectedResult).toEqual(typeOfOrganizationCollection);
      });
    });

    describe('compareTypeOfOrganization', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeOfOrganization(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeOfOrganization(entity1, entity2);
        const compareResult2 = service.compareTypeOfOrganization(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeOfOrganization(entity1, entity2);
        const compareResult2 = service.compareTypeOfOrganization(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeOfOrganization(entity1, entity2);
        const compareResult2 = service.compareTypeOfOrganization(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
