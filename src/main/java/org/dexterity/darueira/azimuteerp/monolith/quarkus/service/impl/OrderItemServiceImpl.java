package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrderItem;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.OrderItemService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrderItemDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.OrderItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final Logger log = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    @Inject
    OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderItemDTO persistOrUpdate(OrderItemDTO orderItemDTO) {
        log.debug("Request to save OrderItem : {}", orderItemDTO);
        var orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItem = OrderItem.persistOrUpdate(orderItem);
        return orderItemMapper.toDto(orderItem);
    }

    /**
     * Delete the OrderItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete OrderItem : {}", id);
        OrderItem.findByIdOptional(id).ifPresent(orderItem -> {
            orderItem.delete();
        });
    }

    /**
     * Get one orderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrderItemDTO> findOne(Long id) {
        log.debug("Request to get OrderItem : {}", id);
        return OrderItem.findByIdOptional(id).map(orderItem -> orderItemMapper.toDto((OrderItem) orderItem));
    }

    /**
     * Get all the orderItems.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OrderItemDTO> findAll(Page page) {
        log.debug("Request to get all OrderItems");
        return new Paged<>(OrderItem.findAll().page(page)).map(orderItem -> orderItemMapper.toDto((OrderItem) orderItem));
    }
}
