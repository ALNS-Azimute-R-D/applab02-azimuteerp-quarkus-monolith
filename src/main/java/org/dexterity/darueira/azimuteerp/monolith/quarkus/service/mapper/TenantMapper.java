package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tenant} and its DTO {@link TenantDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface TenantMapper extends EntityMapper<TenantDTO, Tenant> {
    @Mapping(target = "organizationsLists", ignore = true)
    Tenant toEntity(TenantDTO tenantDTO);

    default Tenant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tenant tenant = new Tenant();
        tenant.id = id;
        return tenant;
    }
}
