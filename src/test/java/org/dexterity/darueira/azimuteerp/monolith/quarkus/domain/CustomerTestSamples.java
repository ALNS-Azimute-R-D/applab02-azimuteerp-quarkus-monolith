package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        Customer customer = new Customer();
        customer.id = 1L;
        customer.customerBusinessCode = "customerBusinessCode1";
        customer.fullname = "fullname1";
        customer.customAttributesDetailsJSON = "customAttributesDetailsJSON1";
        return customer;
    }

    public static Customer getCustomerSample2() {
        Customer customer = new Customer();
        customer.id = 2L;
        customer.customerBusinessCode = "customerBusinessCode2";
        customer.fullname = "fullname2";
        customer.customAttributesDetailsJSON = "customAttributesDetailsJSON2";
        return customer;
    }

    public static Customer getCustomerRandomSampleGenerator() {
        Customer customer = new Customer();
        customer.id = longCount.incrementAndGet();
        customer.customerBusinessCode = UUID.randomUUID().toString();
        customer.fullname = UUID.randomUUID().toString();
        customer.customAttributesDetailsJSON = UUID.randomUUID().toString();
        return customer;
    }
}
