package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { ArticleMapper.class, OrderMapper.class })
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(source = "article.id", target = "articleId")
    @Mapping(source = "article.customName", target = "article")
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.businessCode", target = "order")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(source = "articleId", target = "article")
    @Mapping(source = "orderId", target = "order")
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    default OrderItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.id = id;
        return orderItem;
    }
}
