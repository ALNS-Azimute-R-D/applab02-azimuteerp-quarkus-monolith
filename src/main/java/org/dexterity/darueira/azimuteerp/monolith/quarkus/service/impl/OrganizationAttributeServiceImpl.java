package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationAttribute;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.OrganizationAttributeService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationAttributeDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.OrganizationAttributeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class OrganizationAttributeServiceImpl implements OrganizationAttributeService {

    private final Logger log = LoggerFactory.getLogger(OrganizationAttributeServiceImpl.class);

    @Inject
    OrganizationAttributeMapper organizationAttributeMapper;

    @Override
    @Transactional
    public OrganizationAttributeDTO persistOrUpdate(OrganizationAttributeDTO organizationAttributeDTO) {
        log.debug("Request to save OrganizationAttribute : {}", organizationAttributeDTO);
        var organizationAttribute = organizationAttributeMapper.toEntity(organizationAttributeDTO);
        organizationAttribute = OrganizationAttribute.persistOrUpdate(organizationAttribute);
        return organizationAttributeMapper.toDto(organizationAttribute);
    }

    /**
     * Delete the OrganizationAttribute by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete OrganizationAttribute : {}", id);
        OrganizationAttribute.findByIdOptional(id).ifPresent(organizationAttribute -> {
            organizationAttribute.delete();
        });
    }

    /**
     * Get one organizationAttribute by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrganizationAttributeDTO> findOne(Long id) {
        log.debug("Request to get OrganizationAttribute : {}", id);
        return OrganizationAttribute.findByIdOptional(id).map(
            organizationAttribute -> organizationAttributeMapper.toDto((OrganizationAttribute) organizationAttribute)
        );
    }

    /**
     * Get all the organizationAttributes.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<OrganizationAttributeDTO> findAll(Page page) {
        log.debug("Request to get all OrganizationAttributes");
        return new Paged<>(OrganizationAttribute.findAll().page(page)).map(
            organizationAttribute -> organizationAttributeMapper.toDto((OrganizationAttribute) organizationAttribute)
        );
    }
}
