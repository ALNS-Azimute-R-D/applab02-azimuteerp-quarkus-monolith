package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CustomerTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerType} and its DTO {@link CustomerTypeDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface CustomerTypeMapper extends EntityMapper<CustomerTypeDTO, CustomerType> {
    @Mapping(target = "customersLists", ignore = true)
    CustomerType toEntity(CustomerTypeDTO customerTypeDTO);

    default CustomerType fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerType customerType = new CustomerType();
        customerType.id = id;
        return customerType;
    }
}
