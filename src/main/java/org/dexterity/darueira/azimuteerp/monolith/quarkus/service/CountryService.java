package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CountryDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Country}.
 */
public interface CountryService {
    /**
     * Save a country.
     *
     * @param countryDTO the entity to save.
     * @return the persisted entity.
     */
    CountryDTO persistOrUpdate(CountryDTO countryDTO);

    /**
     * Delete the "id" countryDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the countries.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<CountryDTO> findAll(Page page);

    /**
     * Get the "id" countryDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CountryDTO> findOne(Long id);
}
