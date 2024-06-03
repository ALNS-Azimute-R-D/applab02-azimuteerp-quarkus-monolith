package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMemberRoleAsserts.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMemberRoleTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrganizationMemberRoleMapperTest {

    @Inject
    OrganizationMemberRoleMapper organizationMemberRoleMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrganizationMemberRoleSample1();
        var actual = organizationMemberRoleMapper.toEntity(organizationMemberRoleMapper.toDto(expected));
        assertOrganizationMemberRoleAllPropertiesEquals(expected, actual);
    }
}
