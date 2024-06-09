package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.StockLevelDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.StockLevel}.
 */
public interface StockLevelService {
    /**
     * Save a stockLevel.
     *
     * @param stockLevelDTO the entity to save.
     * @return the persisted entity.
     */
    StockLevelDTO persistOrUpdate(StockLevelDTO stockLevelDTO);

    /**
     * Delete the "id" stockLevelDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the stockLevels.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<StockLevelDTO> findAll(Page page);

    /**
     * Get the "id" stockLevelDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockLevelDTO> findOne(Long id);
}
