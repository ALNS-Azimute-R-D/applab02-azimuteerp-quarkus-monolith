import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IOrganizationMemberRole } from '../organization-member-role.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../organization-member-role.test-samples';

import { OrganizationMemberRoleService, RestOrganizationMemberRole } from './organization-member-role.service';

const requireRestSample: RestOrganizationMemberRole = {
  ...sampleWithRequiredData,
  joinedAt: sampleWithRequiredData.joinedAt?.format(DATE_FORMAT),
};

describe('OrganizationMemberRole Service', () => {
  let service: OrganizationMemberRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrganizationMemberRole | IOrganizationMemberRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrganizationMemberRoleService);
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

    it('should create a OrganizationMemberRole', () => {
      const organizationMemberRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(organizationMemberRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrganizationMemberRole', () => {
      const organizationMemberRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(organizationMemberRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrganizationMemberRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrganizationMemberRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrganizationMemberRole', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrganizationMemberRoleToCollectionIfMissing', () => {
      it('should add a OrganizationMemberRole to an empty array', () => {
        const organizationMemberRole: IOrganizationMemberRole = sampleWithRequiredData;
        expectedResult = service.addOrganizationMemberRoleToCollectionIfMissing([], organizationMemberRole);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationMemberRole);
      });

      it('should not add a OrganizationMemberRole to an array that contains it', () => {
        const organizationMemberRole: IOrganizationMemberRole = sampleWithRequiredData;
        const organizationMemberRoleCollection: IOrganizationMemberRole[] = [
          {
            ...organizationMemberRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrganizationMemberRoleToCollectionIfMissing(organizationMemberRoleCollection, organizationMemberRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrganizationMemberRole to an array that doesn't contain it", () => {
        const organizationMemberRole: IOrganizationMemberRole = sampleWithRequiredData;
        const organizationMemberRoleCollection: IOrganizationMemberRole[] = [sampleWithPartialData];
        expectedResult = service.addOrganizationMemberRoleToCollectionIfMissing(organizationMemberRoleCollection, organizationMemberRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationMemberRole);
      });

      it('should add only unique OrganizationMemberRole to an array', () => {
        const organizationMemberRoleArray: IOrganizationMemberRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const organizationMemberRoleCollection: IOrganizationMemberRole[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationMemberRoleToCollectionIfMissing(
          organizationMemberRoleCollection,
          ...organizationMemberRoleArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const organizationMemberRole: IOrganizationMemberRole = sampleWithRequiredData;
        const organizationMemberRole2: IOrganizationMemberRole = sampleWithPartialData;
        expectedResult = service.addOrganizationMemberRoleToCollectionIfMissing([], organizationMemberRole, organizationMemberRole2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationMemberRole);
        expect(expectedResult).toContain(organizationMemberRole2);
      });

      it('should accept null and undefined values', () => {
        const organizationMemberRole: IOrganizationMemberRole = sampleWithRequiredData;
        expectedResult = service.addOrganizationMemberRoleToCollectionIfMissing([], null, organizationMemberRole, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationMemberRole);
      });

      it('should return initial array if no OrganizationMemberRole is added', () => {
        const organizationMemberRoleCollection: IOrganizationMemberRole[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationMemberRoleToCollectionIfMissing(organizationMemberRoleCollection, undefined, null);
        expect(expectedResult).toEqual(organizationMemberRoleCollection);
      });
    });

    describe('compareOrganizationMemberRole', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrganizationMemberRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrganizationMemberRole(entity1, entity2);
        const compareResult2 = service.compareOrganizationMemberRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrganizationMemberRole(entity1, entity2);
        const compareResult2 = service.compareOrganizationMemberRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrganizationMemberRole(entity1, entity2);
        const compareResult2 = service.compareOrganizationMemberRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
