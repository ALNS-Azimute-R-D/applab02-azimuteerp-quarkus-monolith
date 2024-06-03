package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationRoleAsserts.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationRoleTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrganizationRoleMapperTest {

    @Inject
    OrganizationRoleMapper organizationRoleMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrganizationRoleSample1();
        var actual = organizationRoleMapper.toEntity(organizationRoleMapper.toDto(expected));
        assertOrganizationRoleAllPropertiesEquals(expected, actual);
    }
}
