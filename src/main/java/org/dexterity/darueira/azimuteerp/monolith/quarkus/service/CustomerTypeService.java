package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CustomerTypeDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerType}.
 */
public interface CustomerTypeService {
    /**
     * Save a customerType.
     *
     * @param customerTypeDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerTypeDTO persistOrUpdate(CustomerTypeDTO customerTypeDTO);

    /**
     * Delete the "id" customerTypeDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the customerTypes.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<CustomerTypeDTO> findAll(Page page);

    /**
     * Get the "id" customerTypeDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerTypeDTO> findOne(Long id);
}
