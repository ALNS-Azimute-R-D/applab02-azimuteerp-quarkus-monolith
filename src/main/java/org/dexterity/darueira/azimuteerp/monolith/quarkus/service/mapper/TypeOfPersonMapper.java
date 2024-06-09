package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.TypeOfPersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeOfPerson} and its DTO {@link TypeOfPersonDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface TypeOfPersonMapper extends EntityMapper<TypeOfPersonDTO, TypeOfPerson> {
    @Mapping(target = "personsLists", ignore = true)
    TypeOfPerson toEntity(TypeOfPersonDTO typeOfPersonDTO);

    default TypeOfPerson fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeOfPerson typeOfPerson = new TypeOfPerson();
        typeOfPerson.id = id;
        return typeOfPerson;
    }
}
