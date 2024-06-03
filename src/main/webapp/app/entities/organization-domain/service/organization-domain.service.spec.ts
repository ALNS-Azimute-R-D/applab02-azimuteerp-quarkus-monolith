import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrganizationDomain } from '../organization-domain.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../organization-domain.test-samples';

import { OrganizationDomainService } from './organization-domain.service';

const requireRestSample: IOrganizationDomain = {
  ...sampleWithRequiredData,
};

describe('OrganizationDomain Service', () => {
  let service: OrganizationDomainService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrganizationDomain | IOrganizationDomain[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrganizationDomainService);
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

    it('should create a OrganizationDomain', () => {
      const organizationDomain = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(organizationDomain).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrganizationDomain', () => {
      const organizationDomain = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(organizationDomain).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrganizationDomain', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrganizationDomain', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrganizationDomain', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrganizationDomainToCollectionIfMissing', () => {
      it('should add a OrganizationDomain to an empty array', () => {
        const organizationDomain: IOrganizationDomain = sampleWithRequiredData;
        expectedResult = service.addOrganizationDomainToCollectionIfMissing([], organizationDomain);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationDomain);
      });

      it('should not add a OrganizationDomain to an array that contains it', () => {
        const organizationDomain: IOrganizationDomain = sampleWithRequiredData;
        const organizationDomainCollection: IOrganizationDomain[] = [
          {
            ...organizationDomain,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrganizationDomainToCollectionIfMissing(organizationDomainCollection, organizationDomain);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrganizationDomain to an array that doesn't contain it", () => {
        const organizationDomain: IOrganizationDomain = sampleWithRequiredData;
        const organizationDomainCollection: IOrganizationDomain[] = [sampleWithPartialData];
        expectedResult = service.addOrganizationDomainToCollectionIfMissing(organizationDomainCollection, organizationDomain);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationDomain);
      });

      it('should add only unique OrganizationDomain to an array', () => {
        const organizationDomainArray: IOrganizationDomain[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const organizationDomainCollection: IOrganizationDomain[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationDomainToCollectionIfMissing(organizationDomainCollection, ...organizationDomainArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const organizationDomain: IOrganizationDomain = sampleWithRequiredData;
        const organizationDomain2: IOrganizationDomain = sampleWithPartialData;
        expectedResult = service.addOrganizationDomainToCollectionIfMissing([], organizationDomain, organizationDomain2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationDomain);
        expect(expectedResult).toContain(organizationDomain2);
      });

      it('should accept null and undefined values', () => {
        const organizationDomain: IOrganizationDomain = sampleWithRequiredData;
        expectedResult = service.addOrganizationDomainToCollectionIfMissing([], null, organizationDomain, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationDomain);
      });

      it('should return initial array if no OrganizationDomain is added', () => {
        const organizationDomainCollection: IOrganizationDomain[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationDomainToCollectionIfMissing(organizationDomainCollection, undefined, null);
        expect(expectedResult).toEqual(organizationDomainCollection);
      });
    });

    describe('compareOrganizationDomain', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrganizationDomain(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrganizationDomain(entity1, entity2);
        const compareResult2 = service.compareOrganizationDomain(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrganizationDomain(entity1, entity2);
        const compareResult2 = service.compareOrganizationDomain(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrganizationDomain(entity1, entity2);
        const compareResult2 = service.compareOrganizationDomain(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
