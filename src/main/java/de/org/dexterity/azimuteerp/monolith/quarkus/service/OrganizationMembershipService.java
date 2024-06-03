package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationMembershipDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMembership}.
 */
public interface OrganizationMembershipService {
    /**
     * Save a organizationMembership.
     *
     * @param organizationMembershipDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationMembershipDTO persistOrUpdate(OrganizationMembershipDTO organizationMembershipDTO);

    /**
     * Delete the "id" organizationMembershipDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the organizationMemberships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrganizationMembershipDTO> findAll(Page page);

    /**
     * Get the "id" organizationMembershipDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationMembershipDTO> findOne(Long id);
}
