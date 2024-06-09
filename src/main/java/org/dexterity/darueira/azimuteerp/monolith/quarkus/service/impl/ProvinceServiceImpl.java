package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Province;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.ProvinceService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.ProvinceDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    @Inject
    ProvinceMapper provinceMapper;

    @Override
    @Transactional
    public ProvinceDTO persistOrUpdate(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        var province = provinceMapper.toEntity(provinceDTO);
        province = Province.persistOrUpdate(province);
        return provinceMapper.toDto(province);
    }

    /**
     * Delete the Province by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        Province.findByIdOptional(id).ifPresent(province -> {
            province.delete();
        });
    }

    /**
     * Get one province by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<ProvinceDTO> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return Province.findByIdOptional(id).map(province -> provinceMapper.toDto((Province) province));
    }

    /**
     * Get all the provinces.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<ProvinceDTO> findAll(Page page) {
        log.debug("Request to get all Provinces");
        return new Paged<>(Province.findAll().page(page)).map(province -> provinceMapper.toDto((Province) province));
    }
}
