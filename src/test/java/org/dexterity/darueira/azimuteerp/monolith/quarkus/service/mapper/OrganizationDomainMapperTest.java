package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationDomainAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationDomainTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrganizationDomainMapperTest {

    @Inject
    OrganizationDomainMapper organizationDomainMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrganizationDomainSample1();
        var actual = organizationDomainMapper.toEntity(organizationDomainMapper.toDto(expected));
        assertOrganizationDomainAllPropertiesEquals(expected, actual);
    }
}
