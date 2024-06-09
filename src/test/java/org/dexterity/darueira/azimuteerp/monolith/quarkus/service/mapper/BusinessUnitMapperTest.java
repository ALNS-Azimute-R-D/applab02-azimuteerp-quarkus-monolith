package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.BusinessUnitAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.BusinessUnitTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class BusinessUnitMapperTest {

    @Inject
    BusinessUnitMapper businessUnitMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBusinessUnitSample1();
        var actual = businessUnitMapper.toEntity(businessUnitMapper.toDto(expected));
        assertBusinessUnitAllPropertiesEquals(expected, actual);
    }
}
