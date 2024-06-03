package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationMembershipDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationMembership} and its DTO {@link OrganizationMembershipDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { OrganizationMapper.class, PersonMapper.class })
public interface OrganizationMembershipMapper extends EntityMapper<OrganizationMembershipDTO, OrganizationMembership> {
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organization")
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "person.lastName", target = "person")
    OrganizationMembershipDTO toDto(OrganizationMembership organizationMembership);

    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "personId", target = "person")
    @Mapping(target = "organizationMemberRolesLists", ignore = true)
    OrganizationMembership toEntity(OrganizationMembershipDTO organizationMembershipDTO);

    default OrganizationMembership fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationMembership organizationMembership = new OrganizationMembership();
        organizationMembership.id = id;
        return organizationMembership;
    }
}
