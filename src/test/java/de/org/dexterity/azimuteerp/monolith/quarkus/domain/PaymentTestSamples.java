package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Payment getPaymentSample1() {
        Payment payment = new Payment();
        payment.id = 1L;
        payment.installmentNumber = 1;
        return payment;
    }

    public static Payment getPaymentSample2() {
        Payment payment = new Payment();
        payment.id = 2L;
        payment.installmentNumber = 2;
        return payment;
    }

    public static Payment getPaymentRandomSampleGenerator() {
        Payment payment = new Payment();
        payment.id = longCount.incrementAndGet();
        payment.installmentNumber = intCount.incrementAndGet();
        return payment;
    }
}
