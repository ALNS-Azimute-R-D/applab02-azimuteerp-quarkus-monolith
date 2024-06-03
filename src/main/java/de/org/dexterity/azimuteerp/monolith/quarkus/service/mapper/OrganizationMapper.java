package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Organization} and its DTO {@link OrganizationDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { TenantMapper.class, TypeOfOrganizationMapper.class })
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenant")
    @Mapping(source = "typeOfOrganization.id", target = "typeOfOrganizationId")
    @Mapping(source = "typeOfOrganization.name", target = "typeOfOrganization")
    @Mapping(source = "organizationParent.id", target = "organizationParentId")
    @Mapping(source = "organizationParent.name", target = "organizationParent")
    OrganizationDTO toDto(Organization organization);

    @Mapping(source = "tenantId", target = "tenant")
    @Mapping(source = "typeOfOrganizationId", target = "typeOfOrganization")
    @Mapping(source = "organizationParentId", target = "organizationParent")
    @Mapping(target = "organizationDomainsLists", ignore = true)
    @Mapping(target = "organizationAttributesLists", ignore = true)
    @Mapping(target = "businessUnitsLists", ignore = true)
    @Mapping(target = "childrenOrganizationsLists", ignore = true)
    @Mapping(target = "organizationRolesLists", ignore = true)
    @Mapping(target = "organizationMembershipsLists", ignore = true)
    Organization toEntity(OrganizationDTO organizationDTO);

    default Organization fromId(Long id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.id = id;
        return organization;
    }
}
