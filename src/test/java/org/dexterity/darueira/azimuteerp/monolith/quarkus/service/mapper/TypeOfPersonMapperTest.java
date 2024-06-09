package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TypeOfPersonAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TypeOfPersonTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TypeOfPersonMapperTest {

    @Inject
    TypeOfPersonMapper typeOfPersonMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeOfPersonSample1();
        var actual = typeOfPersonMapper.toEntity(typeOfPersonMapper.toDto(expected));
        assertTypeOfPersonAllPropertiesEquals(expected, actual);
    }
}
