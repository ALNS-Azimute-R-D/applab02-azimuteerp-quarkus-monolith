package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMemberRole;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.OrganizationMemberRoleService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationMemberRoleDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.OrganizationMemberRoleMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class OrganizationMemberRoleServiceImpl implements OrganizationMemberRoleService {

    private final Logger log = LoggerFactory.getLogger(OrganizationMemberRoleServiceImpl.class);

    @Inject
    OrganizationMemberRoleMapper organizationMemberRoleMapper;

    @Override
    @Transactional
    public OrganizationMemberRoleDTO persistOrUpdate(OrganizationMemberRoleDTO organizationMemberRoleDTO) {
        log.debug("Request to save OrganizationMemberRole : {}", organizationMemberRoleDTO);
        var organizationMemberRole = organizationMemberRoleMapper.toEntity(organizationMemberRoleDTO);
        organizationMemberRole = OrganizationMemberRole.persistOrUpdate(organizationMemberRole);
        return organizationMemberRoleMapper.toDto(organizationMemberRole);
    }

    /**
     * Delete the OrganizationMemberRole by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete OrganizationMemberRole : {}", id);
        OrganizationMemberRole.findByIdOptional(id).ifPresent(organizationMemberRole -> {
            organizationMemberRole.delete();
        });
    }

    /**
     * Get one organizationMemberRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrganizationMemberRoleDTO> findOne(Long id) {
        log.debug("Request to get OrganizationMemberRole : {}", id);
        return OrganizationMemberRole.findByIdOptional(id).map(
            organizationMemberRole -> organizationMemberRoleMapper.toDto((OrganizationMemberRole) organizationMemberRole)
        );
    }

    /**
     * Get all the organizationMemberRoles.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OrganizationMemberRoleDTO> findAll(Page page) {
        log.debug("Request to get all OrganizationMemberRoles");
        return new Paged<>(OrganizationMemberRole.findAll().page(page)).map(
            organizationMemberRole -> organizationMemberRoleMapper.toDto((OrganizationMemberRole) organizationMemberRole)
        );
    }
}
