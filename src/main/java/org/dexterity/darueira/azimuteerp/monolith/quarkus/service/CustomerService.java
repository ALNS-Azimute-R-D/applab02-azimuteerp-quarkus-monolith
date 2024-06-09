package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CustomerDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Customer}.
 */
public interface CustomerService {
    /**
     * Save a customer.
     *
     * @param customerDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerDTO persistOrUpdate(CustomerDTO customerDTO);

    /**
     * Delete the "id" customerDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the customers.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<CustomerDTO> findAll(Page page);

    /**
     * Get the "id" customerDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerDTO> findOne(Long id);
}
