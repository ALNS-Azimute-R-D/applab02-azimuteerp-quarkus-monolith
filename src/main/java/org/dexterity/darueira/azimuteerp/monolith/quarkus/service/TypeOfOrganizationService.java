package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.TypeOfOrganizationDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TypeOfOrganization}.
 */
public interface TypeOfOrganizationService {
    /**
     * Save a typeOfOrganization.
     *
     * @param typeOfOrganizationDTO the entity to save.
     * @return the persisted entity.
     */
    TypeOfOrganizationDTO persistOrUpdate(TypeOfOrganizationDTO typeOfOrganizationDTO);

    /**
     * Delete the "id" typeOfOrganizationDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the typeOfOrganizations.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<TypeOfOrganizationDTO> findAll(Page page);

    /**
     * Get the "id" typeOfOrganizationDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeOfOrganizationDTO> findOne(Long id);
}
