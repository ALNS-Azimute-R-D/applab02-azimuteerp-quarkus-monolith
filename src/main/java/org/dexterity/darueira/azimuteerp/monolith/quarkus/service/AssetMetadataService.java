package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetMetadataDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetMetadata}.
 */
public interface AssetMetadataService {
    /**
     * Save a assetMetadata.
     *
     * @param assetMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    AssetMetadataDTO persistOrUpdate(AssetMetadataDTO assetMetadataDTO);

    /**
     * Delete the "id" assetMetadataDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the assetMetadata.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AssetMetadataDTO> findAll(Page page);

    /**
     * Get the "id" assetMetadataDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetMetadataDTO> findOne(Long id);
}
