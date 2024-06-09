import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAsset } from 'app/entities/asset/asset.model';
import { AssetService } from 'app/entities/asset/service/asset.service';
import { AssetCollectionService } from '../service/asset-collection.service';
import { IAssetCollection } from '../asset-collection.model';
import { AssetCollectionFormService } from './asset-collection-form.service';

import { AssetCollectionUpdateComponent } from './asset-collection-update.component';

describe('AssetCollection Management Update Component', () => {
  let comp: AssetCollectionUpdateComponent;
  let fixture: ComponentFixture<AssetCollectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetCollectionFormService: AssetCollectionFormService;
  let assetCollectionService: AssetCollectionService;
  let assetService: AssetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AssetCollectionUpdateComponent],
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
      .overrideTemplate(AssetCollectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetCollectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetCollectionFormService = TestBed.inject(AssetCollectionFormService);
    assetCollectionService = TestBed.inject(AssetCollectionService);
    assetService = TestBed.inject(AssetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Asset query and add missing value', () => {
      const assetCollection: IAssetCollection = { id: 456 };
      const assets: IAsset[] = [{ id: 23536 }];
      assetCollection.assets = assets;

      const assetCollection: IAsset[] = [{ id: 6509 }];
      jest.spyOn(assetService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCollection })));
      const additionalAssets = [...assets];
      const expectedCollection: IAsset[] = [...additionalAssets, ...assetCollection];
      jest.spyOn(assetService, 'addAssetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetCollection });
      comp.ngOnInit();

      expect(assetService.query).toHaveBeenCalled();
      expect(assetService.addAssetToCollectionIfMissing).toHaveBeenCalledWith(
        assetCollection,
        ...additionalAssets.map(expect.objectContaining),
      );
      expect(comp.assetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetCollection: IAssetCollection = { id: 456 };
      const asset: IAsset = { id: 24543 };
      assetCollection.assets = [asset];

      activatedRoute.data = of({ assetCollection });
      comp.ngOnInit();

      expect(comp.assetsSharedCollection).toContain(asset);
      expect(comp.assetCollection).toEqual(assetCollection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetCollection>>();
      const assetCollection = { id: 123 };
      jest.spyOn(assetCollectionFormService, 'getAssetCollection').mockReturnValue(assetCollection);
      jest.spyOn(assetCollectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCollection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetCollection }));
      saveSubject.complete();

      // THEN
      expect(assetCollectionFormService.getAssetCollection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetCollectionService.update).toHaveBeenCalledWith(expect.objectContaining(assetCollection));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetCollection>>();
      const assetCollection = { id: 123 };
      jest.spyOn(assetCollectionFormService, 'getAssetCollection').mockReturnValue({ id: null });
      jest.spyOn(assetCollectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCollection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetCollection }));
      saveSubject.complete();

      // THEN
      expect(assetCollectionFormService.getAssetCollection).toHaveBeenCalled();
      expect(assetCollectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetCollection>>();
      const assetCollection = { id: 123 };
      jest.spyOn(assetCollectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCollection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetCollectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAsset', () => {
      it('Should forward to assetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(assetService, 'compareAsset');
        comp.compareAsset(entity, entity2);
        expect(assetService.compareAsset).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
