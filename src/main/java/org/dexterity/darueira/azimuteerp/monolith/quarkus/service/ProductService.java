package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.ProductDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Product}.
 */
public interface ProductService {
    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    ProductDTO persistOrUpdate(ProductDTO productDTO);

    /**
     * Delete the "id" productDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the products.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<ProductDTO> findAll(Page page);

    /**
     * Get the "id" productDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductDTO> findOne(Long id);

    /**
     * Get all the products with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<ProductDTO> findAllWithEagerRelationships(Page page);
}
