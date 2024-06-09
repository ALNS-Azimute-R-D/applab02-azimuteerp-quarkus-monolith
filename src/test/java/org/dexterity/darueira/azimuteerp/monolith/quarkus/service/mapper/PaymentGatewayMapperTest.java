package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentGatewayAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentGatewayTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PaymentGatewayMapperTest {

    @Inject
    PaymentGatewayMapper paymentGatewayMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentGatewaySample1();
        var actual = paymentGatewayMapper.toEntity(paymentGatewayMapper.toDto(expected));
        assertPaymentGatewayAllPropertiesEquals(expected, actual);
    }
}
