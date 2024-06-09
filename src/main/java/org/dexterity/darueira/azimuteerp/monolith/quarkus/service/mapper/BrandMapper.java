package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.BrandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {
    @Mapping(target = "productsLists", ignore = true)
    Brand toEntity(BrandDTO brandDTO);

    default Brand fromId(Long id) {
        if (id == null) {
            return null;
        }
        Brand brand = new Brand();
        brand.id = id;
        return brand;
    }
}
