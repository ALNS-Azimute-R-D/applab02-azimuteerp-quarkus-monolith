package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { BrandMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "brand.acronym", target = "brand")
    ProductDTO toDto(Product product);

    @Mapping(source = "brandId", target = "brand")
    @Mapping(target = "productsLists", ignore = true)
    @Mapping(target = "stockLevelsLists", ignore = true)
    @Mapping(target = "suppliersLists", ignore = true)
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.id = id;
        return product;
    }
}
