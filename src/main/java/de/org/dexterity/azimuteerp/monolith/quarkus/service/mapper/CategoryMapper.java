package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.CategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(source = "categoryParent.id", target = "categoryParentId")
    @Mapping(source = "categoryParent.acronym", target = "categoryParent")
    CategoryDTO toDto(Category category);

    @Mapping(target = "articlesLists", ignore = true)
    @Mapping(source = "categoryParentId", target = "categoryParent")
    @Mapping(target = "childrenCategoriesLists", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);

    default Category fromId(Long id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.id = id;
        return category;
    }
}
