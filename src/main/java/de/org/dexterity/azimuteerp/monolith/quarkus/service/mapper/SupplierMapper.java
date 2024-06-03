package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.SupplierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Supplier} and its DTO {@link SupplierDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { ProductMapper.class })
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {
    @Mapping(target = "inventoryTransactionsLists", ignore = true)
    Supplier toEntity(SupplierDTO supplierDTO);

    default Supplier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Supplier supplier = new Supplier();
        supplier.id = id;
        return supplier;
    }
}
