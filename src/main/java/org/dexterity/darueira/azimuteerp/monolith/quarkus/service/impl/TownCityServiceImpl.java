package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TownCity;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.TownCityService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.TownCityDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.TownCityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class TownCityServiceImpl implements TownCityService {

    private final Logger log = LoggerFactory.getLogger(TownCityServiceImpl.class);

    @Inject
    TownCityMapper townCityMapper;

    @Override
    @Transactional
    public TownCityDTO persistOrUpdate(TownCityDTO townCityDTO) {
        log.debug("Request to save TownCity : {}", townCityDTO);
        var townCity = townCityMapper.toEntity(townCityDTO);
        townCity = TownCity.persistOrUpdate(townCity);
        return townCityMapper.toDto(townCity);
    }

    /**
     * Delete the TownCity by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete TownCity : {}", id);
        TownCity.findByIdOptional(id).ifPresent(townCity -> {
            townCity.delete();
        });
    }

    /**
     * Get one townCity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<TownCityDTO> findOne(Long id) {
        log.debug("Request to get TownCity : {}", id);
        return TownCity.findByIdOptional(id).map(townCity -> townCityMapper.toDto((TownCity) townCity));
    }

    /**
     * Get all the townCities.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<TownCityDTO> findAll(Page page) {
        log.debug("Request to get all TownCities");
        return new Paged<>(TownCity.findAll().page(page)).map(townCity -> townCityMapper.toDto((TownCity) townCity));
    }
}
