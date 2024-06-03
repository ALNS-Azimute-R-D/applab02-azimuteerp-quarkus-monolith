import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-of-person.test-samples';

import { TypeOfPersonFormService } from './type-of-person-form.service';

describe('TypeOfPerson Form Service', () => {
  let service: TypeOfPersonFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeOfPersonFormService);
  });

  describe('Service methods', () => {
    describe('createTypeOfPersonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeOfPersonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing ITypeOfPerson should create a new form with FormGroup', () => {
        const formGroup = service.createTypeOfPersonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeOfPerson', () => {
      it('should return NewTypeOfPerson for default TypeOfPerson initial value', () => {
        const formGroup = service.createTypeOfPersonFormGroup(sampleWithNewData);

        const typeOfPerson = service.getTypeOfPerson(formGroup) as any;

        expect(typeOfPerson).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeOfPerson for empty TypeOfPerson initial value', () => {
        const formGroup = service.createTypeOfPersonFormGroup();

        const typeOfPerson = service.getTypeOfPerson(formGroup) as any;

        expect(typeOfPerson).toMatchObject({});
      });

      it('should return ITypeOfPerson', () => {
        const formGroup = service.createTypeOfPersonFormGroup(sampleWithRequiredData);

        const typeOfPerson = service.getTypeOfPerson(formGroup) as any;

        expect(typeOfPerson).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeOfPerson should not enable id FormControl', () => {
        const formGroup = service.createTypeOfPersonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeOfPerson should disable id FormControl', () => {
        const formGroup = service.createTypeOfPersonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
