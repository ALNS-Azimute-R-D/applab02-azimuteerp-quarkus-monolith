package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentGatewayTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentGateway getPaymentGatewaySample1() {
        PaymentGateway paymentGateway = new PaymentGateway();
        paymentGateway.id = 1L;
        paymentGateway.aliasCode = "aliasCode1";
        paymentGateway.description = "description1";
        paymentGateway.businessHandlerClazz = "businessHandlerClazz1";
        return paymentGateway;
    }

    public static PaymentGateway getPaymentGatewaySample2() {
        PaymentGateway paymentGateway = new PaymentGateway();
        paymentGateway.id = 2L;
        paymentGateway.aliasCode = "aliasCode2";
        paymentGateway.description = "description2";
        paymentGateway.businessHandlerClazz = "businessHandlerClazz2";
        return paymentGateway;
    }

    public static PaymentGateway getPaymentGatewayRandomSampleGenerator() {
        PaymentGateway paymentGateway = new PaymentGateway();
        paymentGateway.id = longCount.incrementAndGet();
        paymentGateway.aliasCode = UUID.randomUUID().toString();
        paymentGateway.description = UUID.randomUUID().toString();
        paymentGateway.businessHandlerClazz = UUID.randomUUID().toString();
        return paymentGateway;
    }
}
