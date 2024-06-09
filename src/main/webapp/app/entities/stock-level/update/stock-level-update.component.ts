import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { StockLevelService } from '../service/stock-level.service';
import { IStockLevel } from '../stock-level.model';
import { StockLevelFormService, StockLevelFormGroup } from './stock-level-form.service';

@Component({
  standalone: true,
  selector: 'jhi-stock-level-update',
  templateUrl: './stock-level-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StockLevelUpdateComponent implements OnInit {
  isSaving = false;
  stockLevel: IStockLevel | null = null;

  warehousesSharedCollection: IWarehouse[] = [];
  productsSharedCollection: IProduct[] = [];

  protected stockLevelService = inject(StockLevelService);
  protected stockLevelFormService = inject(StockLevelFormService);
  protected warehouseService = inject(WarehouseService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockLevelFormGroup = this.stockLevelFormService.createStockLevelFormGroup();

  compareWarehouse = (o1: IWarehouse | null, o2: IWarehouse | null): boolean => this.warehouseService.compareWarehouse(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockLevel }) => {
      this.stockLevel = stockLevel;
      if (stockLevel) {
        this.updateForm(stockLevel);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stockLevel = this.stockLevelFormService.getStockLevel(this.editForm);
    if (stockLevel.id !== null) {
      this.subscribeToSaveResponse(this.stockLevelService.update(stockLevel));
    } else {
      this.subscribeToSaveResponse(this.stockLevelService.create(stockLevel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockLevel>>): void {
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

  protected updateForm(stockLevel: IStockLevel): void {
    this.stockLevel = stockLevel;
    this.stockLevelFormService.resetForm(this.editForm, stockLevel);

    this.warehousesSharedCollection = this.warehouseService.addWarehouseToCollectionIfMissing<IWarehouse>(
      this.warehousesSharedCollection,
      stockLevel.warehouse,
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      stockLevel.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.warehouseService
      .query()
      .pipe(map((res: HttpResponse<IWarehouse[]>) => res.body ?? []))
      .pipe(
        map((warehouses: IWarehouse[]) =>
          this.warehouseService.addWarehouseToCollectionIfMissing<IWarehouse>(warehouses, this.stockLevel?.warehouse),
        ),
      )
      .subscribe((warehouses: IWarehouse[]) => (this.warehousesSharedCollection = warehouses));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.stockLevel?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
