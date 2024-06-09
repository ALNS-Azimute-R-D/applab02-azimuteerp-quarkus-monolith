package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.SupplierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Supplier} and its DTO {@link SupplierDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { PersonMapper.class })
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {
    @Mapping(source = "representativePerson.id", target = "representativePersonId")
    @Mapping(source = "representativePerson.lastname", target = "representativePerson")
    SupplierDTO toDto(Supplier supplier);

    @Mapping(source = "representativePersonId", target = "representativePerson")
    @Mapping(target = "inventoryTransactionsLists", ignore = true)
    @Mapping(target = "toProducts", ignore = true)
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
