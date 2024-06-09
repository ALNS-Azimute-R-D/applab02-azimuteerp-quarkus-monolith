package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { TypeOfPersonMapper.class, DistrictMapper.class })
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
    @Mapping(source = "typeOfPerson.id", target = "typeOfPersonId")
    @Mapping(source = "typeOfPerson.code", target = "typeOfPerson")
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "district.name", target = "district")
    @Mapping(source = "managerPerson.id", target = "managerPersonId")
    @Mapping(source = "managerPerson.lastname", target = "managerPerson")
    PersonDTO toDto(Person person);

    @Mapping(source = "typeOfPersonId", target = "typeOfPerson")
    @Mapping(source = "districtId", target = "district")
    @Mapping(source = "managerPersonId", target = "managerPerson")
    @Mapping(target = "managedPersonsLists", ignore = true)
    @Mapping(target = "organizationMembershipsLists", ignore = true)
    @Mapping(target = "suppliersLists", ignore = true)
    @Mapping(target = "customersLists", ignore = true)
    Person toEntity(PersonDTO personDTO);

    default Person fromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.id = id;
        return person;
    }
}
