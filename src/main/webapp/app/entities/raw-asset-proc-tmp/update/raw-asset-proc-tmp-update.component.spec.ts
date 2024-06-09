import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAssetType } from 'app/entities/asset-type/asset-type.model';
import { AssetTypeService } from 'app/entities/asset-type/service/asset-type.service';
import { RawAssetProcTmpService } from '../service/raw-asset-proc-tmp.service';
import { IRawAssetProcTmp } from '../raw-asset-proc-tmp.model';
import { RawAssetProcTmpFormService } from './raw-asset-proc-tmp-form.service';

import { RawAssetProcTmpUpdateComponent } from './raw-asset-proc-tmp-update.component';

describe('RawAssetProcTmp Management Update Component', () => {
  let comp: RawAssetProcTmpUpdateComponent;
  let fixture: ComponentFixture<RawAssetProcTmpUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rawAssetProcTmpFormService: RawAssetProcTmpFormService;
  let rawAssetProcTmpService: RawAssetProcTmpService;
  let assetTypeService: AssetTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RawAssetProcTmpUpdateComponent],
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
      .overrideTemplate(RawAssetProcTmpUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RawAssetProcTmpUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rawAssetProcTmpFormService = TestBed.inject(RawAssetProcTmpFormService);
    rawAssetProcTmpService = TestBed.inject(RawAssetProcTmpService);
    assetTypeService = TestBed.inject(AssetTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AssetType query and add missing value', () => {
      const rawAssetProcTmp: IRawAssetProcTmp = { id: 456 };
      const assetType: IAssetType = { id: 28844 };
      rawAssetProcTmp.assetType = assetType;

      const assetTypeCollection: IAssetType[] = [{ id: 32696 }];
      jest.spyOn(assetTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: assetTypeCollection })));
      const additionalAssetTypes = [assetType];
      const expectedCollection: IAssetType[] = [...additionalAssetTypes, ...assetTypeCollection];
      jest.spyOn(assetTypeService, 'addAssetTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rawAssetProcTmp });
      comp.ngOnInit();

      expect(assetTypeService.query).toHaveBeenCalled();
      expect(assetTypeService.addAssetTypeToCollectionIfMissing).toHaveBeenCalledWith(
        assetTypeCollection,
        ...additionalAssetTypes.map(expect.objectContaining),
      );
      expect(comp.assetTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rawAssetProcTmp: IRawAssetProcTmp = { id: 456 };
      const assetType: IAssetType = { id: 20896 };
      rawAssetProcTmp.assetType = assetType;

      activatedRoute.data = of({ rawAssetProcTmp });
      comp.ngOnInit();

      expect(comp.assetTypesSharedCollection).toContain(assetType);
      expect(comp.rawAssetProcTmp).toEqual(rawAssetProcTmp);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRawAssetProcTmp>>();
      const rawAssetProcTmp = { id: 123 };
      jest.spyOn(rawAssetProcTmpFormService, 'getRawAssetProcTmp').mockReturnValue(rawAssetProcTmp);
      jest.spyOn(rawAssetProcTmpService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rawAssetProcTmp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rawAssetProcTmp }));
      saveSubject.complete();

      // THEN
      expect(rawAssetProcTmpFormService.getRawAssetProcTmp).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rawAssetProcTmpService.update).toHaveBeenCalledWith(expect.objectContaining(rawAssetProcTmp));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRawAssetProcTmp>>();
      const rawAssetProcTmp = { id: 123 };
      jest.spyOn(rawAssetProcTmpFormService, 'getRawAssetProcTmp').mockReturnValue({ id: null });
      jest.spyOn(rawAssetProcTmpService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rawAssetProcTmp: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rawAssetProcTmp }));
      saveSubject.complete();

      // THEN
      expect(rawAssetProcTmpFormService.getRawAssetProcTmp).toHaveBeenCalled();
      expect(rawAssetProcTmpService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRawAssetProcTmp>>();
      const rawAssetProcTmp = { id: 123 };
      jest.spyOn(rawAssetProcTmpService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rawAssetProcTmp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rawAssetProcTmpService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAssetType', () => {
      it('Should forward to assetTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(assetTypeService, 'compareAssetType');
        comp.compareAssetType(entity, entity2);
        expect(assetTypeService.compareAssetType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
