package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.WarehouseDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Warehouse}.
 */
public interface WarehouseService {
    /**
     * Save a warehouse.
     *
     * @param warehouseDTO the entity to save.
     * @return the persisted entity.
     */
    WarehouseDTO persistOrUpdate(WarehouseDTO warehouseDTO);

    /**
     * Delete the "id" warehouseDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the warehouses.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<WarehouseDTO> findAll(Page page);

    /**
     * Get the "id" warehouseDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WarehouseDTO> findOne(Long id);
}
