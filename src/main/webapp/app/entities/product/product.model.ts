import { IBrand } from 'app/entities/brand/brand.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { ActivationStatusEnum } from 'app/entities/enumerations/activation-status-enum.model';

export interface IProduct {
  id: number;
  productSKU?: string | null;
  productName?: string | null;
  description?: string | null;
  standardCost?: number | null;
  listPrice?: number | null;
  reorderLevel?: number | null;
  targetLevel?: number | null;
  quantityPerUnit?: string | null;
  discontinued?: boolean | null;
  minimumReorderQuantity?: number | null;
  suggestedCategory?: string | null;
  attachments?: string | null;
  attachmentsContentType?: string | null;
  activationStatus?: keyof typeof ActivationStatusEnum | null;
  brand?: Pick<IBrand, 'id' | 'acronym'> | null;
  toSuppliers?: Pick<ISupplier, 'id'>[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
