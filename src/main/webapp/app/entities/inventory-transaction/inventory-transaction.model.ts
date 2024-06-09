import dayjs from 'dayjs/esm';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { IProduct } from 'app/entities/product/product.model';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IInventoryTransaction {
  id: number;
  invoiceId?: number | null;
  transactionCreatedDate?: dayjs.Dayjs | null;
  transactionModifiedDate?: dayjs.Dayjs | null;
  quantity?: number | null;
  transactionComments?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  supplier?: Pick<ISupplier, 'id' | 'acronym'> | null;
  product?: Pick<IProduct, 'id' | 'productName'> | null;
  warehouse?: Pick<IWarehouse, 'id' | 'acronym'> | null;
}

export type NewInventoryTransaction = Omit<IInventoryTransaction, 'id'> & { id: null };
