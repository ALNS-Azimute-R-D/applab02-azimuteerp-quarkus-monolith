import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';
import { IInventoryTransaction } from '../inventory-transaction.model';
import { InventoryTransactionService } from '../service/inventory-transaction.service';
import { InventoryTransactionFormService } from './inventory-transaction-form.service';

import { InventoryTransactionUpdateComponent } from './inventory-transaction-update.component';

describe('InventoryTransaction Management Update Component', () => {
  let comp: InventoryTransactionUpdateComponent;
  let fixture: ComponentFixture<InventoryTransactionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inventoryTransactionFormService: InventoryTransactionFormService;
  let inventoryTransactionService: InventoryTransactionService;
  let supplierService: SupplierService;
  let productService: ProductService;
  let warehouseService: WarehouseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InventoryTransactionUpdateComponent],
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
      .overrideTemplate(InventoryTransactionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InventoryTransactionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryTransactionFormService = TestBed.inject(InventoryTransactionFormService);
    inventoryTransactionService = TestBed.inject(InventoryTransactionService);
    supplierService = TestBed.inject(SupplierService);
    productService = TestBed.inject(ProductService);
    warehouseService = TestBed.inject(WarehouseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Supplier query and add missing value', () => {
      const inventoryTransaction: IInventoryTransaction = { id: 456 };
      const supplier: ISupplier = { id: 1636 };
      inventoryTransaction.supplier = supplier;

      const supplierCollection: ISupplier[] = [{ id: 12462 }];
      jest.spyOn(supplierService, 'query').mockReturnValue(of(new HttpResponse({ body: supplierCollection })));
      const additionalSuppliers = [supplier];
      const expectedCollection: ISupplier[] = [...additionalSuppliers, ...supplierCollection];
      jest.spyOn(supplierService, 'addSupplierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      expect(supplierService.query).toHaveBeenCalled();
      expect(supplierService.addSupplierToCollectionIfMissing).toHaveBeenCalledWith(
        supplierCollection,
        ...additionalSuppliers.map(expect.objectContaining),
      );
      expect(comp.suppliersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const inventoryTransaction: IInventoryTransaction = { id: 456 };
      const product: IProduct = { id: 26123 };
      inventoryTransaction.product = product;

      const productCollection: IProduct[] = [{ id: 447 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Warehouse query and add missing value', () => {
      const inventoryTransaction: IInventoryTransaction = { id: 456 };
      const warehouse: IWarehouse = { id: 24461 };
      inventoryTransaction.warehouse = warehouse;

      const warehouseCollection: IWarehouse[] = [{ id: 5465 }];
      jest.spyOn(warehouseService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseCollection })));
      const additionalWarehouses = [warehouse];
      const expectedCollection: IWarehouse[] = [...additionalWarehouses, ...warehouseCollection];
      jest.spyOn(warehouseService, 'addWarehouseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      expect(warehouseService.query).toHaveBeenCalled();
      expect(warehouseService.addWarehouseToCollectionIfMissing).toHaveBeenCalledWith(
        warehouseCollection,
        ...additionalWarehouses.map(expect.objectContaining),
      );
      expect(comp.warehousesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inventoryTransaction: IInventoryTransaction = { id: 456 };
      const supplier: ISupplier = { id: 23262 };
      inventoryTransaction.supplier = supplier;
      const product: IProduct = { id: 7337 };
      inventoryTransaction.product = product;
      const warehouse: IWarehouse = { id: 29749 };
      inventoryTransaction.warehouse = warehouse;

      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      expect(comp.suppliersSharedCollection).toContain(supplier);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.warehousesSharedCollection).toContain(warehouse);
      expect(comp.inventoryTransaction).toEqual(inventoryTransaction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryTransaction>>();
      const inventoryTransaction = { id: 123 };
      jest.spyOn(inventoryTransactionFormService, 'getInventoryTransaction').mockReturnValue(inventoryTransaction);
      jest.spyOn(inventoryTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventoryTransaction }));
      saveSubject.complete();

      // THEN
      expect(inventoryTransactionFormService.getInventoryTransaction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryTransactionService.update).toHaveBeenCalledWith(expect.objectContaining(inventoryTransaction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryTransaction>>();
      const inventoryTransaction = { id: 123 };
      jest.spyOn(inventoryTransactionFormService, 'getInventoryTransaction').mockReturnValue({ id: null });
      jest.spyOn(inventoryTransactionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransaction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventoryTransaction }));
      saveSubject.complete();

      // THEN
      expect(inventoryTransactionFormService.getInventoryTransaction).toHaveBeenCalled();
      expect(inventoryTransactionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInventoryTransaction>>();
      const inventoryTransaction = { id: 123 };
      jest.spyOn(inventoryTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryTransactionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSupplier', () => {
      it('Should forward to supplierService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(supplierService, 'compareSupplier');
        comp.compareSupplier(entity, entity2);
        expect(supplierService.compareSupplier).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareWarehouse', () => {
      it('Should forward to warehouseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(warehouseService, 'compareWarehouse');
        comp.compareWarehouse(entity, entity2);
        expect(warehouseService.compareWarehouse).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
