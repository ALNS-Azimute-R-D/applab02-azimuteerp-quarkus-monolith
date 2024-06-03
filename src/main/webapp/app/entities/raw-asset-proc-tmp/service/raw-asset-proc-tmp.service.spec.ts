import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRawAssetProcTmp } from '../raw-asset-proc-tmp.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../raw-asset-proc-tmp.test-samples';

import { RawAssetProcTmpService } from './raw-asset-proc-tmp.service';

const requireRestSample: IRawAssetProcTmp = {
  ...sampleWithRequiredData,
};

describe('RawAssetProcTmp Service', () => {
  let service: RawAssetProcTmpService;
  let httpMock: HttpTestingController;
  let expectedResult: IRawAssetProcTmp | IRawAssetProcTmp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RawAssetProcTmpService);
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

    it('should create a RawAssetProcTmp', () => {
      const rawAssetProcTmp = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rawAssetProcTmp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RawAssetProcTmp', () => {
      const rawAssetProcTmp = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rawAssetProcTmp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RawAssetProcTmp', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RawAssetProcTmp', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RawAssetProcTmp', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRawAssetProcTmpToCollectionIfMissing', () => {
      it('should add a RawAssetProcTmp to an empty array', () => {
        const rawAssetProcTmp: IRawAssetProcTmp = sampleWithRequiredData;
        expectedResult = service.addRawAssetProcTmpToCollectionIfMissing([], rawAssetProcTmp);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rawAssetProcTmp);
      });

      it('should not add a RawAssetProcTmp to an array that contains it', () => {
        const rawAssetProcTmp: IRawAssetProcTmp = sampleWithRequiredData;
        const rawAssetProcTmpCollection: IRawAssetProcTmp[] = [
          {
            ...rawAssetProcTmp,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRawAssetProcTmpToCollectionIfMissing(rawAssetProcTmpCollection, rawAssetProcTmp);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RawAssetProcTmp to an array that doesn't contain it", () => {
        const rawAssetProcTmp: IRawAssetProcTmp = sampleWithRequiredData;
        const rawAssetProcTmpCollection: IRawAssetProcTmp[] = [sampleWithPartialData];
        expectedResult = service.addRawAssetProcTmpToCollectionIfMissing(rawAssetProcTmpCollection, rawAssetProcTmp);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rawAssetProcTmp);
      });

      it('should add only unique RawAssetProcTmp to an array', () => {
        const rawAssetProcTmpArray: IRawAssetProcTmp[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rawAssetProcTmpCollection: IRawAssetProcTmp[] = [sampleWithRequiredData];
        expectedResult = service.addRawAssetProcTmpToCollectionIfMissing(rawAssetProcTmpCollection, ...rawAssetProcTmpArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rawAssetProcTmp: IRawAssetProcTmp = sampleWithRequiredData;
        const rawAssetProcTmp2: IRawAssetProcTmp = sampleWithPartialData;
        expectedResult = service.addRawAssetProcTmpToCollectionIfMissing([], rawAssetProcTmp, rawAssetProcTmp2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rawAssetProcTmp);
        expect(expectedResult).toContain(rawAssetProcTmp2);
      });

      it('should accept null and undefined values', () => {
        const rawAssetProcTmp: IRawAssetProcTmp = sampleWithRequiredData;
        expectedResult = service.addRawAssetProcTmpToCollectionIfMissing([], null, rawAssetProcTmp, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rawAssetProcTmp);
      });

      it('should return initial array if no RawAssetProcTmp is added', () => {
        const rawAssetProcTmpCollection: IRawAssetProcTmp[] = [sampleWithRequiredData];
        expectedResult = service.addRawAssetProcTmpToCollectionIfMissing(rawAssetProcTmpCollection, undefined, null);
        expect(expectedResult).toEqual(rawAssetProcTmpCollection);
      });
    });

    describe('compareRawAssetProcTmp', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRawAssetProcTmp(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRawAssetProcTmp(entity1, entity2);
        const compareResult2 = service.compareRawAssetProcTmp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRawAssetProcTmp(entity1, entity2);
        const compareResult2 = service.compareRawAssetProcTmp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRawAssetProcTmp(entity1, entity2);
        const compareResult2 = service.compareRawAssetProcTmp(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
