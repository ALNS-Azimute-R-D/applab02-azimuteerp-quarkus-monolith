import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrganizationRole } from '../organization-role.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../organization-role.test-samples';

import { OrganizationRoleService } from './organization-role.service';

const requireRestSample: IOrganizationRole = {
  ...sampleWithRequiredData,
};

describe('OrganizationRole Service', () => {
  let service: OrganizationRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrganizationRole | IOrganizationRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrganizationRoleService);
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

    it('should create a OrganizationRole', () => {
      const organizationRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(organizationRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrganizationRole', () => {
      const organizationRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(organizationRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrganizationRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrganizationRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrganizationRole', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrganizationRoleToCollectionIfMissing', () => {
      it('should add a OrganizationRole to an empty array', () => {
        const organizationRole: IOrganizationRole = sampleWithRequiredData;
        expectedResult = service.addOrganizationRoleToCollectionIfMissing([], organizationRole);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationRole);
      });

      it('should not add a OrganizationRole to an array that contains it', () => {
        const organizationRole: IOrganizationRole = sampleWithRequiredData;
        const organizationRoleCollection: IOrganizationRole[] = [
          {
            ...organizationRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrganizationRoleToCollectionIfMissing(organizationRoleCollection, organizationRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrganizationRole to an array that doesn't contain it", () => {
        const organizationRole: IOrganizationRole = sampleWithRequiredData;
        const organizationRoleCollection: IOrganizationRole[] = [sampleWithPartialData];
        expectedResult = service.addOrganizationRoleToCollectionIfMissing(organizationRoleCollection, organizationRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationRole);
      });

      it('should add only unique OrganizationRole to an array', () => {
        const organizationRoleArray: IOrganizationRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const organizationRoleCollection: IOrganizationRole[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationRoleToCollectionIfMissing(organizationRoleCollection, ...organizationRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const organizationRole: IOrganizationRole = sampleWithRequiredData;
        const organizationRole2: IOrganizationRole = sampleWithPartialData;
        expectedResult = service.addOrganizationRoleToCollectionIfMissing([], organizationRole, organizationRole2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(organizationRole);
        expect(expectedResult).toContain(organizationRole2);
      });

      it('should accept null and undefined values', () => {
        const organizationRole: IOrganizationRole = sampleWithRequiredData;
        expectedResult = service.addOrganizationRoleToCollectionIfMissing([], null, organizationRole, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(organizationRole);
      });

      it('should return initial array if no OrganizationRole is added', () => {
        const organizationRoleCollection: IOrganizationRole[] = [sampleWithRequiredData];
        expectedResult = service.addOrganizationRoleToCollectionIfMissing(organizationRoleCollection, undefined, null);
        expect(expectedResult).toEqual(organizationRoleCollection);
      });
    });

    describe('compareOrganizationRole', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrganizationRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrganizationRole(entity1, entity2);
        const compareResult2 = service.compareOrganizationRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrganizationRole(entity1, entity2);
        const compareResult2 = service.compareOrganizationRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrganizationRole(entity1, entity2);
        const compareResult2 = service.compareOrganizationRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
