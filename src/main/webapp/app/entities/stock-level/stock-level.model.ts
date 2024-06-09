import dayjs from 'dayjs/esm';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IStockLevel {
  id: number;
  lastModifiedDate?: dayjs.Dayjs | null;
  remainingQuantity?: number | null;
  commonAttributesDetailsJSON?: string | null;
  warehouse?: Pick<IWarehouse, 'id' | 'acronym'> | null;
  product?: Pick<IProduct, 'id' | 'productName'> | null;
}

export type NewStockLevel = Omit<IStockLevel, 'id'> & { id: null };
