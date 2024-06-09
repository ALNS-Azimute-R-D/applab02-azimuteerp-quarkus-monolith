package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Organization;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.OrganizationService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.OrganizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Inject
    OrganizationMapper organizationMapper;

    @Override
    @Transactional
    public OrganizationDTO persistOrUpdate(OrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);
        var organization = organizationMapper.toEntity(organizationDTO);
        organization = Organization.persistOrUpdate(organization);
        return organizationMapper.toDto(organization);
    }

    /**
     * Delete the Organization by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);
        Organization.findByIdOptional(id).ifPresent(organization -> {
            organization.delete();
        });
    }

    /**
     * Get one organization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrganizationDTO> findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        return Organization.findByIdOptional(id).map(organization -> organizationMapper.toDto((Organization) organization));
    }

    /**
     * Get all the organizations.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OrganizationDTO> findAll(Page page) {
        log.debug("Request to get all Organizations");
        return new Paged<>(Organization.findAll().page(page)).map(organization -> organizationMapper.toDto((Organization) organization));
    }
}
