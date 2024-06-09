package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.District;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.DistrictService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.DistrictDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.DistrictMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class DistrictServiceImpl implements DistrictService {

    private final Logger log = LoggerFactory.getLogger(DistrictServiceImpl.class);

    @Inject
    DistrictMapper districtMapper;

    @Override
    @Transactional
    public DistrictDTO persistOrUpdate(DistrictDTO districtDTO) {
        log.debug("Request to save District : {}", districtDTO);
        var district = districtMapper.toEntity(districtDTO);
        district = District.persistOrUpdate(district);
        return districtMapper.toDto(district);
    }

    /**
     * Delete the District by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete District : {}", id);
        District.findByIdOptional(id).ifPresent(district -> {
            district.delete();
        });
    }

    /**
     * Get one district by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<DistrictDTO> findOne(Long id) {
        log.debug("Request to get District : {}", id);
        return District.findByIdOptional(id).map(district -> districtMapper.toDto((District) district));
    }

    /**
     * Get all the districts.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<DistrictDTO> findAll(Page page) {
        log.debug("Request to get all Districts");
        return new Paged<>(District.findAll().page(page)).map(district -> districtMapper.toDto((District) district));
    }
}
