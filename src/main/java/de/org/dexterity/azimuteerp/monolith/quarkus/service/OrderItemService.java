package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrderItemDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrderItem}.
 */
public interface OrderItemService {
    /**
     * Save a orderItem.
     *
     * @param orderItemDTO the entity to save.
     * @return the persisted entity.
     */
    OrderItemDTO persistOrUpdate(OrderItemDTO orderItemDTO);

    /**
     * Delete the "id" orderItemDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the orderItems.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrderItemDTO> findAll(Page page);

    /**
     * Get the "id" orderItemDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderItemDTO> findOne(Long id);
}
