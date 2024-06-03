package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.AssetTypeDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetType}.
 */
public interface AssetTypeService {
    /**
     * Save a assetType.
     *
     * @param assetTypeDTO the entity to save.
     * @return the persisted entity.
     */
    AssetTypeDTO persistOrUpdate(AssetTypeDTO assetTypeDTO);

    /**
     * Delete the "id" assetTypeDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the assetTypes.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AssetTypeDTO> findAll(Page page);

    /**
     * Get the "id" assetTypeDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetTypeDTO> findOne(Long id);
}
