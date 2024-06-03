package de.org.dexterity.azimuteerp.monolith.quarkus.service;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.CategoryDTO;
import io.quarkus.panache.common.Page;
import java.util.Optional;

/**
 * Service Interface for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Category}.
 */
public interface CategoryService {
    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryDTO persistOrUpdate(CategoryDTO categoryDTO);

    /**
     * Delete the "id" categoryDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the categories.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<CategoryDTO> findAll(Page page);

    /**
     * Get the "id" categoryDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryDTO> findOne(Long id);
}
