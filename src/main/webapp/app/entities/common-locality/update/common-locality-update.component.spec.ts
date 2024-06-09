import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';
import { CommonLocalityService } from '../service/common-locality.service';
import { ICommonLocality } from '../common-locality.model';
import { CommonLocalityFormService } from './common-locality-form.service';

import { CommonLocalityUpdateComponent } from './common-locality-update.component';

describe('CommonLocality Management Update Component', () => {
  let comp: CommonLocalityUpdateComponent;
  let fixture: ComponentFixture<CommonLocalityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commonLocalityFormService: CommonLocalityFormService;
  let commonLocalityService: CommonLocalityService;
  let districtService: DistrictService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CommonLocalityUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommonLocalityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommonLocalityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commonLocalityFormService = TestBed.inject(CommonLocalityFormService);
    commonLocalityService = TestBed.inject(CommonLocalityService);
    districtService = TestBed.inject(DistrictService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call District query and add missing value', () => {
      const commonLocality: ICommonLocality = { id: 456 };
      const district: IDistrict = { id: 20121 };
      commonLocality.district = district;

      const districtCollection: IDistrict[] = [{ id: 23607 }];
      jest.spyOn(districtService, 'query').mockReturnValue(of(new HttpResponse({ body: districtCollection })));
      const additionalDistricts = [district];
      const expectedCollection: IDistrict[] = [...additionalDistricts, ...districtCollection];
      jest.spyOn(districtService, 'addDistrictToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commonLocality });
      comp.ngOnInit();

      expect(districtService.query).toHaveBeenCalled();
      expect(districtService.addDistrictToCollectionIfMissing).toHaveBeenCalledWith(
        districtCollection,
        ...additionalDistricts.map(expect.objectContaining),
      );
      expect(comp.districtsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commonLocality: ICommonLocality = { id: 456 };
      const district: IDistrict = { id: 26074 };
      commonLocality.district = district;

      activatedRoute.data = of({ commonLocality });
      comp.ngOnInit();

      expect(comp.districtsSharedCollection).toContain(district);
      expect(comp.commonLocality).toEqual(commonLocality);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommonLocality>>();
      const commonLocality = { id: 123 };
      jest.spyOn(commonLocalityFormService, 'getCommonLocality').mockReturnValue(commonLocality);
      jest.spyOn(commonLocalityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commonLocality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commonLocality }));
      saveSubject.complete();

      // THEN
      expect(commonLocalityFormService.getCommonLocality).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commonLocalityService.update).toHaveBeenCalledWith(expect.objectContaining(commonLocality));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommonLocality>>();
      const commonLocality = { id: 123 };
      jest.spyOn(commonLocalityFormService, 'getCommonLocality').mockReturnValue({ id: null });
      jest.spyOn(commonLocalityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commonLocality: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commonLocality }));
      saveSubject.complete();

      // THEN
      expect(commonLocalityFormService.getCommonLocality).toHaveBeenCalled();
      expect(commonLocalityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommonLocality>>();
      const commonLocality = { id: 123 };
      jest.spyOn(commonLocalityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commonLocality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commonLocalityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDistrict', () => {
      it('Should forward to districtService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(districtService, 'compareDistrict');
        comp.compareDistrict(entity, entity2);
        expect(districtService.compareDistrict).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
