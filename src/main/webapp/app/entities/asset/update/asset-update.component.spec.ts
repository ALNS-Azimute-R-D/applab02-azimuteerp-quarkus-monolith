import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAssetType } from 'app/entities/asset-type/asset-type.model';
import { AssetTypeService } from 'app/entities/asset-type/service/asset-type.service';
import { IRawAssetProcTmp } from 'app/entities/raw-asset-proc-tmp/raw-asset-proc-tmp.model';
import { RawAssetProcTmpService } from 'app/entities/raw-asset-proc-tmp/service/raw-asset-proc-tmp.service';
import { IAsset } from '../asset.model';
import { AssetService } from '../service/asset.service';
import { AssetFormService } from './asset-form.service';

import { AssetUpdateComponent } from './asset-update.component';

describe('Asset Management Update Component', () => {
  let comp: AssetUpdateComponent;
  let fixture: ComponentFixture<AssetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetFormService: AssetFormService;
  let assetService: AssetService;
  let assetTypeService: AssetTypeService;
  let rawAssetProcTmpService: RawAssetProcTmpService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AssetUpdateComponent],
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
      .overrideTemplate(AssetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetFormService = TestBed.inject(AssetFormService);
    assetService = TestBed.inject(AssetService);
    assetTypeService = TestBed.inject(AssetTypeService);
    rawAssetProcTmpService = TestBed.inject(RawAssetProcTmpService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AssetType query and add missing value', () => {
      const asset: IAsset = { id: 456 };
      const assetType: IAssetType = { id: 27768 };
      asset.assetType = assetType;

      const assetTypeCollection: IAssetType[] = [{ id: 16160 }];
      jest.spyOn(assetTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: assetTypeCollection })));
      const additionalAssetTypes = [assetType];
      const expectedCollection: IAssetType[] = [...additionalAssetTypes, ...assetTypeCollection];
      jest.spyOn(assetTypeService, 'addAssetTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ asset });
      comp.ngOnInit();

      expect(assetTypeService.query).toHaveBeenCalled();
      expect(assetTypeService.addAssetTypeToCollectionIfMissing).toHaveBeenCalledWith(
        assetTypeCollection,
        ...additionalAssetTypes.map(expect.objectContaining),
      );
      expect(comp.assetTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call RawAssetProcTmp query and add missing value', () => {
      const asset: IAsset = { id: 456 };
      const rawAssetProcTmp: IRawAssetProcTmp = { id: 23707 };
      asset.rawAssetProcTmp = rawAssetProcTmp;

      const rawAssetProcTmpCollection: IRawAssetProcTmp[] = [{ id: 18448 }];
      jest.spyOn(rawAssetProcTmpService, 'query').mockReturnValue(of(new HttpResponse({ body: rawAssetProcTmpCollection })));
      const additionalRawAssetProcTmps = [rawAssetProcTmp];
      const expectedCollection: IRawAssetProcTmp[] = [...additionalRawAssetProcTmps, ...rawAssetProcTmpCollection];
      jest.spyOn(rawAssetProcTmpService, 'addRawAssetProcTmpToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ asset });
      comp.ngOnInit();

      expect(rawAssetProcTmpService.query).toHaveBeenCalled();
      expect(rawAssetProcTmpService.addRawAssetProcTmpToCollectionIfMissing).toHaveBeenCalledWith(
        rawAssetProcTmpCollection,
        ...additionalRawAssetProcTmps.map(expect.objectContaining),
      );
      expect(comp.rawAssetProcTmpsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const asset: IAsset = { id: 456 };
      const assetType: IAssetType = { id: 30949 };
      asset.assetType = assetType;
      const rawAssetProcTmp: IRawAssetProcTmp = { id: 9051 };
      asset.rawAssetProcTmp = rawAssetProcTmp;

      activatedRoute.data = of({ asset });
      comp.ngOnInit();

      expect(comp.assetTypesSharedCollection).toContain(assetType);
      expect(comp.rawAssetProcTmpsSharedCollection).toContain(rawAssetProcTmp);
      expect(comp.asset).toEqual(asset);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsset>>();
      const asset = { id: 123 };
      jest.spyOn(assetFormService, 'getAsset').mockReturnValue(asset);
      jest.spyOn(assetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asset });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: asset }));
      saveSubject.complete();

      // THEN
      expect(assetFormService.getAsset).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetService.update).toHaveBeenCalledWith(expect.objectContaining(asset));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsset>>();
      const asset = { id: 123 };
      jest.spyOn(assetFormService, 'getAsset').mockReturnValue({ id: null });
      jest.spyOn(assetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asset: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: asset }));
      saveSubject.complete();

      // THEN
      expect(assetFormService.getAsset).toHaveBeenCalled();
      expect(assetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsset>>();
      const asset = { id: 123 };
      jest.spyOn(assetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asset });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetService.update).toHaveBeenCalled();
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

    describe('compareRawAssetProcTmp', () => {
      it('Should forward to rawAssetProcTmpService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(rawAssetProcTmpService, 'compareRawAssetProcTmp');
        comp.compareRawAssetProcTmp(entity, entity2);
        expect(rawAssetProcTmpService.compareRawAssetProcTmp).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
