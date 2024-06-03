package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.StockLevelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StockLevel} and its DTO {@link StockLevelDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { WarehouseMapper.class, ProductMapper.class })
public interface StockLevelMapper extends EntityMapper<StockLevelDTO, StockLevel> {
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.acronym", target = "warehouse")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.productName", target = "product")
    StockLevelDTO toDto(StockLevel stockLevel);

    @Mapping(source = "warehouseId", target = "warehouse")
    @Mapping(source = "productId", target = "product")
    StockLevel toEntity(StockLevelDTO stockLevelDTO);

    default StockLevel fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockLevel stockLevel = new StockLevel();
        stockLevel.id = id;
        return stockLevel;
    }
}
