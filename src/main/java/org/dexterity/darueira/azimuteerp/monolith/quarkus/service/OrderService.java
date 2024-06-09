package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrderDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Order}.
 */
public interface OrderService {
    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    OrderDTO persistOrUpdate(OrderDTO orderDTO);

    /**
     * Delete the "id" orderDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the orders.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrderDTO> findAll(Page page);

    /**
     * Get the "id" orderDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderDTO> findOne(Long id);
}
