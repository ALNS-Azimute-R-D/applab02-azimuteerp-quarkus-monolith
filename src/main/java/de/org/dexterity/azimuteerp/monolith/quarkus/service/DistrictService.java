package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.DistrictDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.District}.
 */
public interface DistrictService {
    /**
     * Save a district.
     *
     * @param districtDTO the entity to save.
     * @return the persisted entity.
     */
    DistrictDTO persistOrUpdate(DistrictDTO districtDTO);

    /**
     * Delete the "id" districtDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the districts.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<DistrictDTO> findAll(Page page);

    /**
     * Get the "id" districtDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DistrictDTO> findOne(Long id);
}
