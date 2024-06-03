package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationRoleDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationRole}.
 */
public interface OrganizationRoleService {
    /**
     * Save a organizationRole.
     *
     * @param organizationRoleDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationRoleDTO persistOrUpdate(OrganizationRoleDTO organizationRoleDTO);

    /**
     * Delete the "id" organizationRoleDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the organizationRoles.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrganizationRoleDTO> findAll(Page page);

    /**
     * Get the "id" organizationRoleDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationRoleDTO> findOne(Long id);
}
