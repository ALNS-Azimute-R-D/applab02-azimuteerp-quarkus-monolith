package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationAttributeAsserts.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationAttributeTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrganizationAttributeMapperTest {

    @Inject
    OrganizationAttributeMapper organizationAttributeMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrganizationAttributeSample1();
        var actual = organizationAttributeMapper.toEntity(organizationAttributeMapper.toDto(expected));
        assertOrganizationAttributeAllPropertiesEquals(expected, actual);
    }
}
