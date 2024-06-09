package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { InvoiceMapper.class, CustomerMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(source = "invoice.id", target = "invoiceId")
    @Mapping(source = "invoice.description", target = "invoice")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.fullname", target = "customer")
    OrderDTO toDto(Order order);

    @Mapping(target = "orderItemsLists", ignore = true)
    @Mapping(source = "invoiceId", target = "invoice")
    @Mapping(source = "customerId", target = "customer")
    Order toEntity(OrderDTO orderDTO);

    default Order fromId(Long id) {
        if (id == null) {
            return null;
        }
        Order order = new Order();
        order.id = id;
        return order;
    }
}
