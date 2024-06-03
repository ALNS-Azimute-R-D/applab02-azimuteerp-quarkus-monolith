import { IArticle } from 'app/entities/article/article.model';
import { IOrder } from 'app/entities/order/order.model';
import { OrderItemStatusEnum } from 'app/entities/enumerations/order-item-status-enum.model';

export interface IOrderItem {
  id: number;
  quantity?: number | null;
  totalPrice?: number | null;
  status?: keyof typeof OrderItemStatusEnum | null;
  article?: Pick<IArticle, 'id' | 'customName'> | null;
  order?: Pick<IOrder, 'id' | 'businessCode'> | null;
}

export type NewOrderItem = Omit<IOrderItem, 'id'> & { id: null };
