package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationMembership;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.OrganizationMembershipService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationMembershipDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.OrganizationMembershipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class OrganizationMembershipServiceImpl implements OrganizationMembershipService {

    private final Logger log = LoggerFactory.getLogger(OrganizationMembershipServiceImpl.class);

    @Inject
    OrganizationMembershipMapper organizationMembershipMapper;

    @Override
    @Transactional
    public OrganizationMembershipDTO persistOrUpdate(OrganizationMembershipDTO organizationMembershipDTO) {
        log.debug("Request to save OrganizationMembership : {}", organizationMembershipDTO);
        var organizationMembership = organizationMembershipMapper.toEntity(organizationMembershipDTO);
        organizationMembership = OrganizationMembership.persistOrUpdate(organizationMembership);
        return organizationMembershipMapper.toDto(organizationMembership);
    }

    /**
     * Delete the OrganizationMembership by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete OrganizationMembership : {}", id);
        OrganizationMembership.findByIdOptional(id).ifPresent(organizationMembership -> {
            organizationMembership.delete();
        });
    }

    /**
     * Get one organizationMembership by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrganizationMembershipDTO> findOne(Long id) {
        log.debug("Request to get OrganizationMembership : {}", id);
        return OrganizationMembership.findByIdOptional(id).map(
            organizationMembership -> organizationMembershipMapper.toDto((OrganizationMembership) organizationMembership)
        );
    }

    /**
     * Get all the organizationMemberships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OrganizationMembershipDTO> findAll(Page page) {
        log.debug("Request to get all OrganizationMemberships");
        return new Paged<>(OrganizationMembership.findAll().page(page)).map(
            organizationMembership -> organizationMembershipMapper.toDto((OrganizationMembership) organizationMembership)
        );
    }
}
