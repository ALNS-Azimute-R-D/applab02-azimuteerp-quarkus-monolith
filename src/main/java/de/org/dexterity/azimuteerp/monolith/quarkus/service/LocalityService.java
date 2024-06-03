package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.LocalityDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Locality}.
 */
public interface LocalityService {
    /**
     * Save a locality.
     *
     * @param localityDTO the entity to save.
     * @return the persisted entity.
     */
    LocalityDTO persistOrUpdate(LocalityDTO localityDTO);

    /**
     * Delete the "id" localityDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the localities.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<LocalityDTO> findAll(Page page);

    /**
     * Get the "id" localityDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocalityDTO> findOne(Long id);
}
