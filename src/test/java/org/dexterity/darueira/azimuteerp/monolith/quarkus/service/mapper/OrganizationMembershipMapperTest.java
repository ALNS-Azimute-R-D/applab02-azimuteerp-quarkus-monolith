package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationMembershipAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationMembershipTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrganizationMembershipMapperTest {

    @Inject
    OrganizationMembershipMapper organizationMembershipMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrganizationMembershipSample1();
        var actual = organizationMembershipMapper.toEntity(organizationMembershipMapper.toDto(expected));
        assertOrganizationMembershipAllPropertiesEquals(expected, actual);
    }
}
