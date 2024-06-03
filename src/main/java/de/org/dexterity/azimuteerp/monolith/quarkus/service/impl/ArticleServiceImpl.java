package de.org.dexterity.azimuteerp.monolith.quarkus.service.impl;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.Article;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.ArticleService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.ArticleDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper.ArticleMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Inject
    ArticleMapper articleMapper;

    @Override
    @Transactional
    public ArticleDTO persistOrUpdate(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        var article = articleMapper.toEntity(articleDTO);
        article = Article.persistOrUpdate(article);
        return articleMapper.toDto(article);
    }

    /**
     * Delete the Article by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        Article.findByIdOptional(id).ifPresent(article -> {
            article.delete();
        });
    }

    /**
     * Get one article by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<ArticleDTO> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return Article.findByIdOptional(id).map(article -> articleMapper.toDto((Article) article));
    }

    /**
     * Get all the articles.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<ArticleDTO> findAll(Page page) {
        log.debug("Request to get all Articles");
        return new Paged<>(Article.findAll().page(page)).map(article -> articleMapper.toDto((Article) article));
    }
}
