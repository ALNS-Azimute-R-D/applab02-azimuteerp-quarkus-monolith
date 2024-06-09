package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationDomainDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationDomain}.
 */
public interface OrganizationDomainService {
    /**
     * Save a organizationDomain.
     *
     * @param organizationDomainDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationDomainDTO persistOrUpdate(OrganizationDomainDTO organizationDomainDTO);

    /**
     * Delete the "id" organizationDomainDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the organizationDomains.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrganizationDomainDTO> findAll(Page page);

    /**
     * Get the "id" organizationDomainDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationDomainDTO> findOne(Long id);
}
