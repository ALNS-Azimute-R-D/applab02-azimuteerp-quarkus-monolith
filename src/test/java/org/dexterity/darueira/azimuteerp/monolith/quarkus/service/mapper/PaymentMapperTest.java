package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PaymentMapperTest {

    @Inject
    PaymentMapper paymentMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentSample1();
        var actual = paymentMapper.toEntity(paymentMapper.toDto(expected));
        assertPaymentAllPropertiesEquals(expected, actual);
    }
}
