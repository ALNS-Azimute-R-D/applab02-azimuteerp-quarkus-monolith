import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';
import { InventoryTransactionService } from '../service/inventory-transaction.service';
import { IInventoryTransaction } from '../inventory-transaction.model';
import { InventoryTransactionFormService, InventoryTransactionFormGroup } from './inventory-transaction-form.service';

@Component({
  standalone: true,
  selector: 'jhi-inventory-transaction-update',
  templateUrl: './inventory-transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InventoryTransactionUpdateComponent implements OnInit {
  isSaving = false;
  inventoryTransaction: IInventoryTransaction | null = null;
  activationStatusEnumValues = Object.keys(ActivationStatusEnum);

  suppliersSharedCollection: ISupplier[] = [];
  productsSharedCollection: IProduct[] = [];
  warehousesSharedCollection: IWarehouse[] = [];

  protected inventoryTransactionService = inject(InventoryTransactionService);
  protected inventoryTransactionFormService = inject(InventoryTransactionFormService);
  protected supplierService = inject(SupplierService);
  protected productService = inject(ProductService);
  protected warehouseService = inject(WarehouseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryTransactionFormGroup = this.inventoryTransactionFormService.createInventoryTransactionFormGroup();

  compareSupplier = (o1: ISupplier | null, o2: ISupplier | null): boolean => this.supplierService.compareSupplier(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareWarehouse = (o1: IWarehouse | null, o2: IWarehouse | null): boolean => this.warehouseService.compareWarehouse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryTransaction }) => {
      this.inventoryTransaction = inventoryTransaction;
      if (inventoryTransaction) {
        this.updateForm(inventoryTransaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventoryTransaction = this.inventoryTransactionFormService.getInventoryTransaction(this.editForm);
    if (inventoryTransaction.id !== null) {
      this.subscribeToSaveResponse(this.inventoryTransactionService.update(inventoryTransaction));
    } else {
      this.subscribeToSaveResponse(this.inventoryTransactionService.create(inventoryTransaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventoryTransaction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(inventoryTransaction: IInventoryTransaction): void {
    this.inventoryTransaction = inventoryTransaction;
    this.inventoryTransactionFormService.resetForm(this.editForm, inventoryTransaction);

    this.suppliersSharedCollection = this.supplierService.addSupplierToCollectionIfMissing<ISupplier>(
      this.suppliersSharedCollection,
      inventoryTransaction.supplier,
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      inventoryTransaction.product,
    );
    this.warehousesSharedCollection = this.warehouseService.addWarehouseToCollectionIfMissing<IWarehouse>(
      this.warehousesSharedCollection,
      inventoryTransaction.warehouse,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.supplierService
      .query()
      .pipe(map((res: HttpResponse<ISupplier[]>) => res.body ?? []))
      .pipe(
        map((suppliers: ISupplier[]) =>
          this.supplierService.addSupplierToCollectionIfMissing<ISupplier>(suppliers, this.inventoryTransaction?.supplier),
        ),
      )
      .subscribe((suppliers: ISupplier[]) => (this.suppliersSharedCollection = suppliers));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.inventoryTransaction?.product),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.warehouseService
      .query()
      .pipe(map((res: HttpResponse<IWarehouse[]>) => res.body ?? []))
      .pipe(
        map((warehouses: IWarehouse[]) =>
          this.warehouseService.addWarehouseToCollectionIfMissing<IWarehouse>(warehouses, this.inventoryTransaction?.warehouse),
        ),
      )
      .subscribe((warehouses: IWarehouse[]) => (this.warehousesSharedCollection = warehouses));
  }
}
