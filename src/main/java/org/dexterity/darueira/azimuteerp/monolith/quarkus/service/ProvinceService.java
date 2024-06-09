package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.ProvinceDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Province}.
 */
public interface ProvinceService {
    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save.
     * @return the persisted entity.
     */
    ProvinceDTO persistOrUpdate(ProvinceDTO provinceDTO);

    /**
     * Delete the "id" provinceDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the provinces.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<ProvinceDTO> findAll(Page page);

    /**
     * Get the "id" provinceDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProvinceDTO> findOne(Long id);
}
