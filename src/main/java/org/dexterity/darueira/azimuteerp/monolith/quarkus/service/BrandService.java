package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.BrandDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Brand}.
 */
public interface BrandService {
    /**
     * Save a brand.
     *
     * @param brandDTO the entity to save.
     * @return the persisted entity.
     */
    BrandDTO persistOrUpdate(BrandDTO brandDTO);

    /**
     * Delete the "id" brandDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the brands.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<BrandDTO> findAll(Page page);

    /**
     * Get the "id" brandDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BrandDTO> findOne(Long id);
}
