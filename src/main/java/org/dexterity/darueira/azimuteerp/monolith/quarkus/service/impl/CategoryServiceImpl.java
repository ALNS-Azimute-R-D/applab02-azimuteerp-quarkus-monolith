package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.impl;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Category;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.CategoryService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CategoryDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Inject
    CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDTO persistOrUpdate(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        var category = categoryMapper.toEntity(categoryDTO);
        category = Category.persistOrUpdate(category);
        return categoryMapper.toDto(category);
    }

    /**
     * Delete the Category by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        Category.findByIdOptional(id).ifPresent(category -> {
            category.delete();
        });
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<CategoryDTO> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        return Category.findByIdOptional(id).map(category -> categoryMapper.toDto((Category) category));
    }

    /**
     * Get all the categories.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<CategoryDTO> findAll(Page page) {
        log.debug("Request to get all Categories");
        return new Paged<>(Category.findAll().page(page)).map(category -> categoryMapper.toDto((Category) category));
    }
}
