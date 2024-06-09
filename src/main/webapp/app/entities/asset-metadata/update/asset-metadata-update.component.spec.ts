import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAsset } from 'app/entities/asset/asset.model';
import { AssetService } from 'app/entities/asset/service/asset.service';
import { AssetMetadataService } from '../service/asset-metadata.service';
import { IAssetMetadata } from '../asset-metadata.model';
import { AssetMetadataFormService } from './asset-metadata-form.service';

import { AssetMetadataUpdateComponent } from './asset-metadata-update.component';

describe('AssetMetadata Management Update Component', () => {
  let comp: AssetMetadataUpdateComponent;
  let fixture: ComponentFixture<AssetMetadataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetMetadataFormService: AssetMetadataFormService;
  let assetMetadataService: AssetMetadataService;
  let assetService: AssetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AssetMetadataUpdateComponent],
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
      .overrideTemplate(AssetMetadataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetMetadataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetMetadataFormService = TestBed.inject(AssetMetadataFormService);
    assetMetadataService = TestBed.inject(AssetMetadataService);
    assetService = TestBed.inject(AssetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call asset query and add missing value', () => {
      const assetMetadata: IAssetMetadata = { id: 456 };
      const asset: IAsset = { id: 26515 };
      assetMetadata.asset = asset;

      const assetCollection: IAsset[] = [{ id: 4516 }];
      jest.spyOn(assetService, 'query').mockReturnValue(of(new HttpResponse({ body: assetCollection })));
      const expectedCollection: IAsset[] = [asset, ...assetCollection];
      jest.spyOn(assetService, 'addAssetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetMetadata });
      comp.ngOnInit();

      expect(assetService.query).toHaveBeenCalled();
      expect(assetService.addAssetToCollectionIfMissing).toHaveBeenCalledWith(assetCollection, asset);
      expect(comp.assetsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetMetadata: IAssetMetadata = { id: 456 };
      const asset: IAsset = { id: 11249 };
      assetMetadata.asset = asset;

      activatedRoute.data = of({ assetMetadata });
      comp.ngOnInit();

      expect(comp.assetsCollection).toContain(asset);
      expect(comp.assetMetadata).toEqual(assetMetadata);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetMetadata>>();
      const assetMetadata = { id: 123 };
      jest.spyOn(assetMetadataFormService, 'getAssetMetadata').mockReturnValue(assetMetadata);
      jest.spyOn(assetMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetMetadata }));
      saveSubject.complete();

      // THEN
      expect(assetMetadataFormService.getAssetMetadata).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetMetadataService.update).toHaveBeenCalledWith(expect.objectContaining(assetMetadata));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetMetadata>>();
      const assetMetadata = { id: 123 };
      jest.spyOn(assetMetadataFormService, 'getAssetMetadata').mockReturnValue({ id: null });
      jest.spyOn(assetMetadataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetMetadata: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetMetadata }));
      saveSubject.complete();

      // THEN
      expect(assetMetadataFormService.getAssetMetadata).toHaveBeenCalled();
      expect(assetMetadataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetMetadata>>();
      const assetMetadata = { id: 123 };
      jest.spyOn(assetMetadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetMetadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetMetadataService.update).toHaveBeenCalled();
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
