package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationRole;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.OrganizationRoleService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationRoleDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.OrganizationRoleMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class OrganizationRoleServiceImpl implements OrganizationRoleService {

    private final Logger log = LoggerFactory.getLogger(OrganizationRoleServiceImpl.class);

    @Inject
    OrganizationRoleMapper organizationRoleMapper;

    @Override
    @Transactional
    public OrganizationRoleDTO persistOrUpdate(OrganizationRoleDTO organizationRoleDTO) {
        log.debug("Request to save OrganizationRole : {}", organizationRoleDTO);
        var organizationRole = organizationRoleMapper.toEntity(organizationRoleDTO);
        organizationRole = OrganizationRole.persistOrUpdate(organizationRole);
        return organizationRoleMapper.toDto(organizationRole);
    }

    /**
     * Delete the OrganizationRole by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete OrganizationRole : {}", id);
        OrganizationRole.findByIdOptional(id).ifPresent(organizationRole -> {
            organizationRole.delete();
        });
    }

    /**
     * Get one organizationRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrganizationRoleDTO> findOne(Long id) {
        log.debug("Request to get OrganizationRole : {}", id);
        return OrganizationRole.findByIdOptional(id).map(
            organizationRole -> organizationRoleMapper.toDto((OrganizationRole) organizationRole)
        );
    }

    /**
     * Get all the organizationRoles.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OrganizationRoleDTO> findAll(Page page) {
        log.debug("Request to get all OrganizationRoles");
        return new Paged<>(OrganizationRole.findAll().page(page)).map(
            organizationRole -> organizationRoleMapper.toDto((OrganizationRole) organizationRole)
        );
    }
}
