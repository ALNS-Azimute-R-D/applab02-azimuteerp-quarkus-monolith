package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationMemberRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationMemberRole} and its DTO {@link OrganizationMemberRoleDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { OrganizationMembershipMapper.class, OrganizationRoleMapper.class })
public interface OrganizationMemberRoleMapper extends EntityMapper<OrganizationMemberRoleDTO, OrganizationMemberRole> {
    @Mapping(source = "organizationMembership.id", target = "organizationMembershipId")
    @Mapping(source = "organizationMembership.id", target = "organizationMembership")
    @Mapping(source = "organizationRole.id", target = "organizationRoleId")
    @Mapping(source = "organizationRole.id", target = "organizationRole")
    OrganizationMemberRoleDTO toDto(OrganizationMemberRole organizationMemberRole);

    @Mapping(source = "organizationMembershipId", target = "organizationMembership")
    @Mapping(source = "organizationRoleId", target = "organizationRole")
    OrganizationMemberRole toEntity(OrganizationMemberRoleDTO organizationMemberRoleDTO);

    default OrganizationMemberRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationMemberRole organizationMemberRole = new OrganizationMemberRole();
        organizationMemberRole.id = id;
        return organizationMemberRole;
    }
}
