package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.ArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { CategoryMapper.class })
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {
    @Mapping(source = "mainCategory.id", target = "mainCategoryId")
    @Mapping(source = "mainCategory.acronym", target = "mainCategory")
    ArticleDTO toDto(Article article);

    @Mapping(source = "mainCategoryId", target = "mainCategory")
    @Mapping(target = "ordersItemsLists", ignore = true)
    Article toEntity(ArticleDTO articleDTO);

    default Article fromId(Long id) {
        if (id == null) {
            return null;
        }
        Article article = new Article();
        article.id = id;
        return article;
    }
}
