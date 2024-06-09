package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.InventoryTransaction;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.InventoryTransactionService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.InventoryTransactionDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.InventoryTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final Logger log = LoggerFactory.getLogger(InventoryTransactionServiceImpl.class);

    @Inject
    InventoryTransactionMapper inventoryTransactionMapper;

    @Override
    @Transactional
    public InventoryTransactionDTO persistOrUpdate(InventoryTransactionDTO inventoryTransactionDTO) {
        log.debug("Request to save InventoryTransaction : {}", inventoryTransactionDTO);
        var inventoryTransaction = inventoryTransactionMapper.toEntity(inventoryTransactionDTO);
        inventoryTransaction = InventoryTransaction.persistOrUpdate(inventoryTransaction);
        return inventoryTransactionMapper.toDto(inventoryTransaction);
    }

    /**
     * Delete the InventoryTransaction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete InventoryTransaction : {}", id);
        InventoryTransaction.findByIdOptional(id).ifPresent(inventoryTransaction -> {
            inventoryTransaction.delete();
        });
    }

    /**
     * Get one inventoryTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<InventoryTransactionDTO> findOne(Long id) {
        log.debug("Request to get InventoryTransaction : {}", id);
        return InventoryTransaction.findByIdOptional(id).map(
            inventoryTransaction -> inventoryTransactionMapper.toDto((InventoryTransaction) inventoryTransaction)
        );
    }

    /**
     * Get all the inventoryTransactions.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<InventoryTransactionDTO> findAll(Page page) {
        log.debug("Request to get all InventoryTransactions");
        return new Paged<>(InventoryTransaction.findAll().page(page)).map(
            inventoryTransaction -> inventoryTransactionMapper.toDto((InventoryTransaction) inventoryTransaction)
        );
    }
}
