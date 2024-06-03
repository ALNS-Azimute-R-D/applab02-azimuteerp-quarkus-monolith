package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.RawAssetProcTmpDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.RawAssetProcTmp}.
 */
public interface RawAssetProcTmpService {
    /**
     * Save a rawAssetProcTmp.
     *
     * @param rawAssetProcTmpDTO the entity to save.
     * @return the persisted entity.
     */
    RawAssetProcTmpDTO persistOrUpdate(RawAssetProcTmpDTO rawAssetProcTmpDTO);

    /**
     * Delete the "id" rawAssetProcTmpDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the rawAssetProcTmps.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<RawAssetProcTmpDTO> findAll(Page page);

    /**
     * Get the "id" rawAssetProcTmpDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RawAssetProcTmpDTO> findOne(Long id);
}
