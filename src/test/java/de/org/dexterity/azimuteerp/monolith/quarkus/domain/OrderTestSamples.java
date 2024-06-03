package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Order getOrderSample1() {
        Order order = new Order();
        order.id = 1L;
        order.businessCode = "businessCode1";
        order.customerUserId = "customerUserId1";
        order.invoiceId = 1L;
        return order;
    }

    public static Order getOrderSample2() {
        Order order = new Order();
        order.id = 2L;
        order.businessCode = "businessCode2";
        order.customerUserId = "customerUserId2";
        order.invoiceId = 2L;
        return order;
    }

    public static Order getOrderRandomSampleGenerator() {
        Order order = new Order();
        order.id = longCount.incrementAndGet();
        order.businessCode = UUID.randomUUID().toString();
        order.customerUserId = UUID.randomUUID().toString();
        order.invoiceId = longCount.incrementAndGet();
        return order;
    }
}
