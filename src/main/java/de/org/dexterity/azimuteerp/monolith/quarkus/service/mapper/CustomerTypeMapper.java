package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.CustomerTypeDTO;
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
