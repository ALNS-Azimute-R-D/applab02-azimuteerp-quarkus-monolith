package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Organization}.
 */
public interface OrganizationService {
    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationDTO persistOrUpdate(OrganizationDTO organizationDTO);

    /**
     * Delete the "id" organizationDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the organizations.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrganizationDTO> findAll(Page page);

    /**
     * Get the "id" organizationDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationDTO> findOne(Long id);
}
