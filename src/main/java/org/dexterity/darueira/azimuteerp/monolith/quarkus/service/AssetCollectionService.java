package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetCollectionDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetCollection}.
 */
public interface AssetCollectionService {
    /**
     * Save a assetCollection.
     *
     * @param assetCollectionDTO the entity to save.
     * @return the persisted entity.
     */
    AssetCollectionDTO persistOrUpdate(AssetCollectionDTO assetCollectionDTO);

    /**
     * Delete the "id" assetCollectionDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the assetCollections.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AssetCollectionDTO> findAll(Page page);

    /**
     * Get the "id" assetCollectionDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetCollectionDTO> findOne(Long id);

    /**
     * Get all the assetCollections with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AssetCollectionDTO> findAllWithEagerRelationships(Page page);
}
