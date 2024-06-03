package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.BusinessUnitDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.BusinessUnit}.
 */
public interface BusinessUnitService {
    /**
     * Save a businessUnit.
     *
     * @param businessUnitDTO the entity to save.
     * @return the persisted entity.
     */
    BusinessUnitDTO persistOrUpdate(BusinessUnitDTO businessUnitDTO);

    /**
     * Delete the "id" businessUnitDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the businessUnits.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<BusinessUnitDTO> findAll(Page page);

    /**
     * Get the "id" businessUnitDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessUnitDTO> findOne(Long id);
}
