package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomerType getCustomerTypeSample1() {
        CustomerType customerType = new CustomerType();
        customerType.id = 1L;
        customerType.name = "name1";
        customerType.description = "description1";
        customerType.businessHandlerClazz = "businessHandlerClazz1";
        return customerType;
    }

    public static CustomerType getCustomerTypeSample2() {
        CustomerType customerType = new CustomerType();
        customerType.id = 2L;
        customerType.name = "name2";
        customerType.description = "description2";
        customerType.businessHandlerClazz = "businessHandlerClazz2";
        return customerType;
    }

    public static CustomerType getCustomerTypeRandomSampleGenerator() {
        CustomerType customerType = new CustomerType();
        customerType.id = longCount.incrementAndGet();
        customerType.name = UUID.randomUUID().toString();
        customerType.description = UUID.randomUUID().toString();
        customerType.businessHandlerClazz = UUID.randomUUID().toString();
        return customerType;
    }
}
