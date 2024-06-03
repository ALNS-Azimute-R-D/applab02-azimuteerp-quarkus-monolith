package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.BusinessUnit;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.BusinessUnitService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.BusinessUnitDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.BusinessUnitMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class BusinessUnitServiceImpl implements BusinessUnitService {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitServiceImpl.class);

    @Inject
    BusinessUnitMapper businessUnitMapper;

    @Override
    @Transactional
    public BusinessUnitDTO persistOrUpdate(BusinessUnitDTO businessUnitDTO) {
        log.debug("Request to save BusinessUnit : {}", businessUnitDTO);
        var businessUnit = businessUnitMapper.toEntity(businessUnitDTO);
        businessUnit = BusinessUnit.persistOrUpdate(businessUnit);
        return businessUnitMapper.toDto(businessUnit);
    }

    /**
     * Delete the BusinessUnit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete BusinessUnit : {}", id);
        BusinessUnit.findByIdOptional(id).ifPresent(businessUnit -> {
            businessUnit.delete();
        });
    }

    /**
     * Get one businessUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<BusinessUnitDTO> findOne(Long id) {
        log.debug("Request to get BusinessUnit : {}", id);
        return BusinessUnit.findByIdOptional(id).map(businessUnit -> businessUnitMapper.toDto((BusinessUnit) businessUnit));
    }

    /**
     * Get all the businessUnits.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<BusinessUnitDTO> findAll(Page page) {
        log.debug("Request to get all BusinessUnits");
        return new Paged<>(BusinessUnit.findAll().page(page)).map(businessUnit -> businessUnitMapper.toDto((BusinessUnit) businessUnit));
    }
}
