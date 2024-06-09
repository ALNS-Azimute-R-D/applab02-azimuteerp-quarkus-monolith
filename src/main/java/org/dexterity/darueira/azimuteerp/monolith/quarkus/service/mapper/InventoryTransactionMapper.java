package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.InventoryTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoryTransaction} and its DTO {@link InventoryTransactionDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { SupplierMapper.class, ProductMapper.class, WarehouseMapper.class })
public interface InventoryTransactionMapper extends EntityMapper<InventoryTransactionDTO, InventoryTransaction> {
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.acronym", target = "supplier")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.productName", target = "product")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.acronym", target = "warehouse")
    InventoryTransactionDTO toDto(InventoryTransaction inventoryTransaction);

    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "productId", target = "product")
    @Mapping(source = "warehouseId", target = "warehouse")
    InventoryTransaction toEntity(InventoryTransactionDTO inventoryTransactionDTO);

    default InventoryTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.id = id;
        return inventoryTransaction;
    }
}
