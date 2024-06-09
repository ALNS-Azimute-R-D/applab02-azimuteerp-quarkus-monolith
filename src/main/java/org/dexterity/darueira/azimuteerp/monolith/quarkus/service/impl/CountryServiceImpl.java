package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Country;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.CountryService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CountryDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.CountryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    @Inject
    CountryMapper countryMapper;

    @Override
    @Transactional
    public CountryDTO persistOrUpdate(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        var country = countryMapper.toEntity(countryDTO);
        country = Country.persistOrUpdate(country);
        return countryMapper.toDto(country);
    }

    /**
     * Delete the Country by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        Country.findByIdOptional(id).ifPresent(country -> {
            country.delete();
        });
    }

    /**
     * Get one country by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<CountryDTO> findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        return Country.findByIdOptional(id).map(country -> countryMapper.toDto((Country) country));
    }

    /**
     * Get all the countries.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<CountryDTO> findAll(Page page) {
        log.debug("Request to get all Countries");
        return new Paged<>(Country.findAll().page(page)).map(country -> countryMapper.toDto((Country) country));
    }
}
