package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationMemberRoleDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMemberRole}.
 */
public interface OrganizationMemberRoleService {
    /**
     * Save a organizationMemberRole.
     *
     * @param organizationMemberRoleDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationMemberRoleDTO persistOrUpdate(OrganizationMemberRoleDTO organizationMemberRoleDTO);

    /**
     * Delete the "id" organizationMemberRoleDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the organizationMemberRoles.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrganizationMemberRoleDTO> findAll(Page page);

    /**
     * Get the "id" organizationMemberRoleDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationMemberRoleDTO> findOne(Long id);
}
