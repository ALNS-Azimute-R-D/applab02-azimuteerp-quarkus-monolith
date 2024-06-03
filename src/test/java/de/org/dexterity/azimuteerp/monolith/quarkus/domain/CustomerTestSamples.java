package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

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
        customer.name = "name1";
        customer.email = "email1";
        customer.addressDetails = "addressDetails1";
        customer.zipCode = "zipCode1";
        customer.keycloakGroupDetails = "keycloakGroupDetails1";
        return customer;
    }

    public static Customer getCustomerSample2() {
        Customer customer = new Customer();
        customer.id = 2L;
        customer.customerBusinessCode = "customerBusinessCode2";
        customer.name = "name2";
        customer.email = "email2";
        customer.addressDetails = "addressDetails2";
        customer.zipCode = "zipCode2";
        customer.keycloakGroupDetails = "keycloakGroupDetails2";
        return customer;
    }

    public static Customer getCustomerRandomSampleGenerator() {
        Customer customer = new Customer();
        customer.id = longCount.incrementAndGet();
        customer.customerBusinessCode = UUID.randomUUID().toString();
        customer.name = UUID.randomUUID().toString();
        customer.email = UUID.randomUUID().toString();
        customer.addressDetails = UUID.randomUUID().toString();
        customer.zipCode = UUID.randomUUID().toString();
        customer.keycloakGroupDetails = UUID.randomUUID().toString();
        return customer;
    }
}
