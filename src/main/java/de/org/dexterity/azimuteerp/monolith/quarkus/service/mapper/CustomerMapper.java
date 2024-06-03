package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { CustomerTypeMapper.class, DistrictMapper.class })
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(source = "customerType.id", target = "customerTypeId")
    @Mapping(source = "customerType.name", target = "customerType")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "district.name", target = "district")
    CustomerDTO toDto(Customer customer);

    @Mapping(source = "customerTypeId", target = "customerType")
    @Mapping(source = "districtId", target = "district")
    Customer toEntity(CustomerDTO customerDTO);

    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.id = id;
        return customer;
    }
}
