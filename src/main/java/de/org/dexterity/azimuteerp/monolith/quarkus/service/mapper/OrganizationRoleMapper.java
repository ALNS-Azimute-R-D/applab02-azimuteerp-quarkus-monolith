package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationRole} and its DTO {@link OrganizationRoleDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { OrganizationMapper.class })
public interface OrganizationRoleMapper extends EntityMapper<OrganizationRoleDTO, OrganizationRole> {
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organization")
    OrganizationRoleDTO toDto(OrganizationRole organizationRole);

    @Mapping(source = "organizationId", target = "organization")
    @Mapping(target = "organizationMemberRolesLists", ignore = true)
    OrganizationRole toEntity(OrganizationRoleDTO organizationRoleDTO);

    default OrganizationRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationRole organizationRole = new OrganizationRole();
        organizationRole.id = id;
        return organizationRole;
    }
}
