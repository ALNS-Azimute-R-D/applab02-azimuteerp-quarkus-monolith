package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { PersonMapper.class, CustomerTypeMapper.class, DistrictMapper.class })
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(source = "buyerPerson.id", target = "buyerPersonId")
    @Mapping(source = "buyerPerson.lastname", target = "buyerPerson")
    @Mapping(source = "customerType.id", target = "customerTypeId")
    @Mapping(source = "customerType.name", target = "customerType")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "district.name", target = "district")
    CustomerDTO toDto(Customer customer);

    @Mapping(source = "buyerPersonId", target = "buyerPerson")
    @Mapping(source = "customerTypeId", target = "customerType")
    @Mapping(source = "districtId", target = "district")
    @Mapping(target = "ordersLists", ignore = true)
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
