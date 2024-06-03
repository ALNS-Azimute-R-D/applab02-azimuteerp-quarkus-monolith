package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.Order;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.OrderService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrderDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.OrderMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Inject
    OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO persistOrUpdate(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        var order = orderMapper.toEntity(orderDTO);
        order = Order.persistOrUpdate(order);
        return orderMapper.toDto(order);
    }

    /**
     * Delete the Order by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        Order.findByIdOptional(id).ifPresent(order -> {
            order.delete();
        });
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return Order.findByIdOptional(id).map(order -> orderMapper.toDto((Order) order));
    }

    /**
     * Get all the orders.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OrderDTO> findAll(Page page) {
        log.debug("Request to get all Orders");
        return new Paged<>(Order.findAll().page(page)).map(order -> orderMapper.toDto((Order) order));
    }
}
