package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TypeOfOrganizationAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TypeOfOrganizationTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TypeOfOrganizationMapperTest {

    @Inject
    TypeOfOrganizationMapper typeOfOrganizationMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeOfOrganizationSample1();
        var actual = typeOfOrganizationMapper.toEntity(typeOfOrganizationMapper.toDto(expected));
        assertTypeOfOrganizationAllPropertiesEquals(expected, actual);
    }
}
