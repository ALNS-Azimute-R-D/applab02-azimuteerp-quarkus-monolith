import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IStockLevel } from '../stock-level.model';
import { StockLevelService } from '../service/stock-level.service';
import { StockLevelFormService } from './stock-level-form.service';

import { StockLevelUpdateComponent } from './stock-level-update.component';

describe('StockLevel Management Update Component', () => {
  let comp: StockLevelUpdateComponent;
  let fixture: ComponentFixture<StockLevelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stockLevelFormService: StockLevelFormService;
  let stockLevelService: StockLevelService;
  let warehouseService: WarehouseService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), StockLevelUpdateComponent],
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
      .overrideTemplate(StockLevelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StockLevelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockLevelFormService = TestBed.inject(StockLevelFormService);
    stockLevelService = TestBed.inject(StockLevelService);
    warehouseService = TestBed.inject(WarehouseService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Warehouse query and add missing value', () => {
      const stockLevel: IStockLevel = { id: 456 };
      const warehouse: IWarehouse = { id: 28389 };
      stockLevel.warehouse = warehouse;

      const warehouseCollection: IWarehouse[] = [{ id: 906 }];
      jest.spyOn(warehouseService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseCollection })));
      const additionalWarehouses = [warehouse];
      const expectedCollection: IWarehouse[] = [...additionalWarehouses, ...warehouseCollection];
      jest.spyOn(warehouseService, 'addWarehouseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockLevel });
      comp.ngOnInit();

      expect(warehouseService.query).toHaveBeenCalled();
      expect(warehouseService.addWarehouseToCollectionIfMissing).toHaveBeenCalledWith(
        warehouseCollection,
        ...additionalWarehouses.map(expect.objectContaining),
      );
      expect(comp.warehousesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const stockLevel: IStockLevel = { id: 456 };
      const product: IProduct = { id: 30497 };
      stockLevel.product = product;

      const productCollection: IProduct[] = [{ id: 24232 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockLevel });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const stockLevel: IStockLevel = { id: 456 };
      const warehouse: IWarehouse = { id: 12010 };
      stockLevel.warehouse = warehouse;
      const product: IProduct = { id: 14399 };
      stockLevel.product = product;

      activatedRoute.data = of({ stockLevel });
      comp.ngOnInit();

      expect(comp.warehousesSharedCollection).toContain(warehouse);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.stockLevel).toEqual(stockLevel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockLevel>>();
      const stockLevel = { id: 123 };
      jest.spyOn(stockLevelFormService, 'getStockLevel').mockReturnValue(stockLevel);
      jest.spyOn(stockLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockLevel }));
      saveSubject.complete();

      // THEN
      expect(stockLevelFormService.getStockLevel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockLevelService.update).toHaveBeenCalledWith(expect.objectContaining(stockLevel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockLevel>>();
      const stockLevel = { id: 123 };
      jest.spyOn(stockLevelFormService, 'getStockLevel').mockReturnValue({ id: null });
      jest.spyOn(stockLevelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockLevel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockLevel }));
      saveSubject.complete();

      // THEN
      expect(stockLevelFormService.getStockLevel).toHaveBeenCalled();
      expect(stockLevelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockLevel>>();
      const stockLevel = { id: 123 };
      jest.spyOn(stockLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockLevelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWarehouse', () => {
      it('Should forward to warehouseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(warehouseService, 'compareWarehouse');
        comp.compareWarehouse(entity, entity2);
        expect(warehouseService.compareWarehouse).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
