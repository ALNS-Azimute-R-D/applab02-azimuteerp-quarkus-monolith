package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.SupplierDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Supplier}.
 */
public interface SupplierService {
    /**
     * Save a supplier.
     *
     * @param supplierDTO the entity to save.
     * @return the persisted entity.
     */
    SupplierDTO persistOrUpdate(SupplierDTO supplierDTO);

    /**
     * Delete the "id" supplierDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the suppliers.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<SupplierDTO> findAll(Page page);

    /**
     * Get the "id" supplierDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierDTO> findOne(Long id);

    /**
     * Get all the suppliers with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<SupplierDTO> findAllWithEagerRelationships(Page page);
}
