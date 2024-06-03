package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.BusinessUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessUnit} and its DTO {@link BusinessUnitDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { OrganizationMapper.class })
public interface BusinessUnitMapper extends EntityMapper<BusinessUnitDTO, BusinessUnit> {
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organization")
    @Mapping(source = "businessUnitParent.id", target = "businessUnitParentId")
    @Mapping(source = "businessUnitParent.name", target = "businessUnitParent")
    BusinessUnitDTO toDto(BusinessUnit businessUnit);

    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "businessUnitParentId", target = "businessUnitParent")
    @Mapping(target = "childrenBusinessUnitsLists", ignore = true)
    BusinessUnit toEntity(BusinessUnitDTO businessUnitDTO);

    default BusinessUnit fromId(Long id) {
        if (id == null) {
            return null;
        }
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.id = id;
        return businessUnit;
    }
}
