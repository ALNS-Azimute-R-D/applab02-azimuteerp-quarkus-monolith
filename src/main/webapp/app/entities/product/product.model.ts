import { IBrand } from 'app/entities/brand/brand.model';

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
  supplierIds?: string | null;
  brand?: Pick<IBrand, 'id' | 'acronym'> | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
