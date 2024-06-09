package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationDomain;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.OrganizationDomainService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationDomainDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.OrganizationDomainMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class OrganizationDomainServiceImpl implements OrganizationDomainService {

    private final Logger log = LoggerFactory.getLogger(OrganizationDomainServiceImpl.class);

    @Inject
    OrganizationDomainMapper organizationDomainMapper;

    @Override
    @Transactional
    public OrganizationDomainDTO persistOrUpdate(OrganizationDomainDTO organizationDomainDTO) {
        log.debug("Request to save OrganizationDomain : {}", organizationDomainDTO);
        var organizationDomain = organizationDomainMapper.toEntity(organizationDomainDTO);
        organizationDomain = OrganizationDomain.persistOrUpdate(organizationDomain);
        return organizationDomainMapper.toDto(organizationDomain);
    }

    /**
     * Delete the OrganizationDomain by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete OrganizationDomain : {}", id);
        OrganizationDomain.findByIdOptional(id).ifPresent(organizationDomain -> {
            organizationDomain.delete();
        });
    }

    /**
     * Get one organizationDomain by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrganizationDomainDTO> findOne(Long id) {
        log.debug("Request to get OrganizationDomain : {}", id);
        return OrganizationDomain.findByIdOptional(id).map(
            organizationDomain -> organizationDomainMapper.toDto((OrganizationDomain) organizationDomain)
        );
    }

    /**
     * Get all the organizationDomains.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OrganizationDomainDTO> findAll(Page page) {
        log.debug("Request to get all OrganizationDomains");
        return new Paged<>(OrganizationDomain.findAll().page(page)).map(
            organizationDomain -> organizationDomainMapper.toDto((OrganizationDomain) organizationDomain)
        );
    }
}
