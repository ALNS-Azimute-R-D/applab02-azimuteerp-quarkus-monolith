import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AssetTypeService } from '../service/asset-type.service';
import { IAssetType } from '../asset-type.model';
import { AssetTypeFormService } from './asset-type-form.service';

import { AssetTypeUpdateComponent } from './asset-type-update.component';

describe('AssetType Management Update Component', () => {
  let comp: AssetTypeUpdateComponent;
  let fixture: ComponentFixture<AssetTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetTypeFormService: AssetTypeFormService;
  let assetTypeService: AssetTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AssetTypeUpdateComponent],
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
      .overrideTemplate(AssetTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetTypeFormService = TestBed.inject(AssetTypeFormService);
    assetTypeService = TestBed.inject(AssetTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const assetType: IAssetType = { id: 456 };

      activatedRoute.data = of({ assetType });
      comp.ngOnInit();

      expect(comp.assetType).toEqual(assetType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetType>>();
      const assetType = { id: 123 };
      jest.spyOn(assetTypeFormService, 'getAssetType').mockReturnValue(assetType);
      jest.spyOn(assetTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetType }));
      saveSubject.complete();

      // THEN
      expect(assetTypeFormService.getAssetType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetTypeService.update).toHaveBeenCalledWith(expect.objectContaining(assetType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetType>>();
      const assetType = { id: 123 };
      jest.spyOn(assetTypeFormService, 'getAssetType').mockReturnValue({ id: null });
      jest.spyOn(assetTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetType }));
      saveSubject.complete();

      // THEN
      expect(assetTypeFormService.getAssetType).toHaveBeenCalled();
      expect(assetTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAssetType>>();
      const assetType = { id: 123 };
      jest.spyOn(assetTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
