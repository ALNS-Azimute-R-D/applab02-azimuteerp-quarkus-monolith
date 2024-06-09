package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationAttributeDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationAttribute}.
 */
public interface OrganizationAttributeService {
    /**
     * Save a organizationAttribute.
     *
     * @param organizationAttributeDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationAttributeDTO persistOrUpdate(OrganizationAttributeDTO organizationAttributeDTO);

    /**
     * Delete the "id" organizationAttributeDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the organizationAttributes.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrganizationAttributeDTO> findAll(Page page);

    /**
     * Get the "id" organizationAttributeDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationAttributeDTO> findOne(Long id);
}
