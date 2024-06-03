package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.InventoryTransactionDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.InventoryTransaction}.
 */
public interface InventoryTransactionService {
    /**
     * Save a inventoryTransaction.
     *
     * @param inventoryTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    InventoryTransactionDTO persistOrUpdate(InventoryTransactionDTO inventoryTransactionDTO);

    /**
     * Delete the "id" inventoryTransactionDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the inventoryTransactions.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<InventoryTransactionDTO> findAll(Page page);

    /**
     * Get the "id" inventoryTransactionDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InventoryTransactionDTO> findOne(Long id);
}
