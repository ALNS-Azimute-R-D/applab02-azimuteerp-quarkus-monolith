package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CommonLocalityDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CommonLocality}.
 */
public interface CommonLocalityService {
    /**
     * Save a commonLocality.
     *
     * @param commonLocalityDTO the entity to save.
     * @return the persisted entity.
     */
    CommonLocalityDTO persistOrUpdate(CommonLocalityDTO commonLocalityDTO);

    /**
     * Delete the "id" commonLocalityDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the commonLocalities.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<CommonLocalityDTO> findAll(Page page);

    /**
     * Get the "id" commonLocalityDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommonLocalityDTO> findOne(Long id);
}
