import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IOrganizationMembership } from '../organization-membership.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../organization-membership.test-samples';

import { OrganizationMembershipService, RestOrganizationMembership } from './organization-membership.service';

const requireRestSample: RestOrganizationMembership = {
  ...sampleWithRequiredData,
  joinedAt: sampleWithRequiredData.joinedAt?.format(DATE_FORMAT),
};

describe('OrganizationMembership Service', () => {
  let service: OrganizationMembershipService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrganizationMembership | IOrganizationMembership[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrganizationMembershipService);
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

    it('should create a OrganizationMembership', () => {
      const organizationMembership = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(organizationMembership).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrganizationMembership', () => {
      const organizationMembership = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(organizationMembership).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrganizationMembership', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrganizationMembership', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrganizationMembership', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrganizationMembershipToCollectionIfMissing', () => {
      it('should add a OrganizationMembership to an empty array', () => {
        const organizationMembership: IOrganizationMembership = sampleWithRequiredData;
        expectedResult = service.addOrganizationMembershipToCollectionIfMissing([], organizationMembership);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationMembership);
      });

      it('should not add a OrganizationMembership to an array that contains it', () => {
        const organizationMembership: IOrganizationMembership = sampleWithRequiredData;
        const organizationMembershipCollection: IOrganizationMembership[] = [
          {
            ...organizationMembership,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrganizationMembershipToCollectionIfMissing(organizationMembershipCollection, organizationMembership);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrganizationMembership to an array that doesn't contain it", () => {
        const organizationMembership: IOrganizationMembership = sampleWithRequiredData;
        const organizationMembershipCollection: IOrganizationMembership[] = [sampleWithPartialData];
        expectedResult = service.addOrganizationMembershipToCollectionIfMissing(organizationMembershipCollection, organizationMembership);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationMembership);
      });

      it('should add only unique OrganizationMembership to an array', () => {
        const organizationMembershipArray: IOrganizationMembership[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const organizationMembershipCollection: IOrganizationMembership[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationMembershipToCollectionIfMissing(
          organizationMembershipCollection,
          ...organizationMembershipArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const organizationMembership: IOrganizationMembership = sampleWithRequiredData;
        const organizationMembership2: IOrganizationMembership = sampleWithPartialData;
        expectedResult = service.addOrganizationMembershipToCollectionIfMissing([], organizationMembership, organizationMembership2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationMembership);
        expect(expectedResult).toContain(organizationMembership2);
      });

      it('should accept null and undefined values', () => {
        const organizationMembership: IOrganizationMembership = sampleWithRequiredData;
        expectedResult = service.addOrganizationMembershipToCollectionIfMissing([], null, organizationMembership, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationMembership);
      });

      it('should return initial array if no OrganizationMembership is added', () => {
        const organizationMembershipCollection: IOrganizationMembership[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationMembershipToCollectionIfMissing(organizationMembershipCollection, undefined, null);
        expect(expectedResult).toEqual(organizationMembershipCollection);
      });
    });

    describe('compareOrganizationMembership', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrganizationMembership(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrganizationMembership(entity1, entity2);
        const compareResult2 = service.compareOrganizationMembership(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrganizationMembership(entity1, entity2);
        const compareResult2 = service.compareOrganizationMembership(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrganizationMembership(entity1, entity2);
        const compareResult2 = service.compareOrganizationMembership(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
