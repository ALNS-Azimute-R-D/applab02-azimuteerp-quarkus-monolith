package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TownCityDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.TownCity}.
 */
public interface TownCityService {
    /**
     * Save a townCity.
     *
     * @param townCityDTO the entity to save.
     * @return the persisted entity.
     */
    TownCityDTO persistOrUpdate(TownCityDTO townCityDTO);

    /**
     * Delete the "id" townCityDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the townCities.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<TownCityDTO> findAll(Page page);

    /**
     * Get the "id" townCityDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TownCityDTO> findOne(Long id);
}
