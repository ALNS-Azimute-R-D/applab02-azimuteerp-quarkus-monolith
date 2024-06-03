package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentMethodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentMethod getPaymentMethodSample1() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.id = 1L;
        paymentMethod.code = "code1";
        paymentMethod.description = "description1";
        paymentMethod.businessHandlerClazz = "businessHandlerClazz1";
        return paymentMethod;
    }

    public static PaymentMethod getPaymentMethodSample2() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.id = 2L;
        paymentMethod.code = "code2";
        paymentMethod.description = "description2";
        paymentMethod.businessHandlerClazz = "businessHandlerClazz2";
        return paymentMethod;
    }

    public static PaymentMethod getPaymentMethodRandomSampleGenerator() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.id = longCount.incrementAndGet();
        paymentMethod.code = UUID.randomUUID().toString();
        paymentMethod.description = UUID.randomUUID().toString();
        paymentMethod.businessHandlerClazz = UUID.randomUUID().toString();
        return paymentMethod;
    }
}
