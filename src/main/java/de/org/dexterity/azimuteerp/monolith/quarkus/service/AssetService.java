package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.AssetDTO;
import io.quarkus.panache.common.Page;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Asset}.
 */
public interface AssetService {
    /**
     * Save a asset.
     *
     * @param assetDTO the entity to save.
     * @return the persisted entity.
     */
    AssetDTO persistOrUpdate(AssetDTO assetDTO);

    /**
     * Delete the "id" assetDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the assets.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AssetDTO> findAll(Page page);

    /**
     * Get the "id" assetDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetDTO> findOne(Long id);

    /**
     * Get all the AssetDTO where AssetMetadata is {@code null}.
     *
     * @return the list of entities.
     */
    List<AssetDTO> findAllWhereAssetMetadataIsNull();
}
