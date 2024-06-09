package org.dexterity.darueira.azimuteerp.monolith.quarkus.service;

import io.quarkus.panache.common.Page;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.ArticleDTO;

/**
 * Service Interface for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Article}.
 */
public interface ArticleService {
    /**
     * Save a article.
     *
     * @param articleDTO the entity to save.
     * @return the persisted entity.
     */
    ArticleDTO persistOrUpdate(ArticleDTO articleDTO);

    /**
     * Delete the "id" articleDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the articles.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<ArticleDTO> findAll(Page page);

    /**
     * Get the "id" articleDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArticleDTO> findOne(Long id);

    /**
     * Get all the articles with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<ArticleDTO> findAllWithEagerRelationships(Page page);
}
