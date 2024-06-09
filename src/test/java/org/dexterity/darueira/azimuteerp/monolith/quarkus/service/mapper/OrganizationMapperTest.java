package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrganizationMapperTest {

    @Inject
    OrganizationMapper organizationMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrganizationSample1();
        var actual = organizationMapper.toEntity(organizationMapper.toDto(expected));
        assertOrganizationAllPropertiesEquals(expected, actual);
    }
}
