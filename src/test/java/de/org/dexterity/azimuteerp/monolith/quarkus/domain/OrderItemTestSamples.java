package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OrderItem getOrderItemSample1() {
        OrderItem orderItem = new OrderItem();
        orderItem.id = 1L;
        orderItem.quantity = 1;
        return orderItem;
    }

    public static OrderItem getOrderItemSample2() {
        OrderItem orderItem = new OrderItem();
        orderItem.id = 2L;
        orderItem.quantity = 2;
        return orderItem;
    }

    public static OrderItem getOrderItemRandomSampleGenerator() {
        OrderItem orderItem = new OrderItem();
        orderItem.id = longCount.incrementAndGet();
        orderItem.quantity = intCount.incrementAndGet();
        return orderItem;
    }
}
