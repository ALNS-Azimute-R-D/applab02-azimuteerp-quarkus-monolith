package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CommonLocality;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.CommonLocalityService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CommonLocalityDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.CommonLocalityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class CommonLocalityServiceImpl implements CommonLocalityService {

    private final Logger log = LoggerFactory.getLogger(CommonLocalityServiceImpl.class);

    @Inject
    CommonLocalityMapper commonLocalityMapper;

    @Override
    @Transactional
    public CommonLocalityDTO persistOrUpdate(CommonLocalityDTO commonLocalityDTO) {
        log.debug("Request to save CommonLocality : {}", commonLocalityDTO);
        var commonLocality = commonLocalityMapper.toEntity(commonLocalityDTO);
        commonLocality = CommonLocality.persistOrUpdate(commonLocality);
        return commonLocalityMapper.toDto(commonLocality);
    }

    /**
     * Delete the CommonLocality by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete CommonLocality : {}", id);
        CommonLocality.findByIdOptional(id).ifPresent(commonLocality -> {
            commonLocality.delete();
        });
    }

    /**
     * Get one commonLocality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<CommonLocalityDTO> findOne(Long id) {
        log.debug("Request to get CommonLocality : {}", id);
        return CommonLocality.findByIdOptional(id).map(commonLocality -> commonLocalityMapper.toDto((CommonLocality) commonLocality));
    }

    /**
     * Get all the commonLocalities.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<CommonLocalityDTO> findAll(Page page) {
        log.debug("Request to get all CommonLocalities");
        return new Paged<>(CommonLocality.findAll().page(page)).map(
            commonLocality -> commonLocalityMapper.toDto((CommonLocality) commonLocality)
        );
    }
}
