package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerTypeAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerTypeTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CustomerTypeMapperTest {

    @Inject
    CustomerTypeMapper customerTypeMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCustomerTypeSample1();
        var actual = customerTypeMapper.toEntity(customerTypeMapper.toDto(expected));
        assertCustomerTypeAllPropertiesEquals(expected, actual);
    }
}
