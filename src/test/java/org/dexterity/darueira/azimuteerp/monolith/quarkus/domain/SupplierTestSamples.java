package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SupplierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Supplier getSupplierSample1() {
        Supplier supplier = new Supplier();
        supplier.id = 1L;
        supplier.acronym = "acronym1";
        supplier.companyName = "companyName1";
        supplier.streetAddress = "streetAddress1";
        supplier.houseNumber = "houseNumber1";
        supplier.locationName = "locationName1";
        supplier.city = "city1";
        supplier.stateProvince = "stateProvince1";
        supplier.zipPostalCode = "zipPostalCode1";
        supplier.countryRegion = "countryRegion1";
        supplier.mainEmail = "mainEmail1";
        supplier.phoneNumber1 = "phoneNumber11";
        supplier.phoneNumber2 = "phoneNumber21";
        supplier.customAttributesDetailsJSON = "customAttributesDetailsJSON1";
        return supplier;
    }

    public static Supplier getSupplierSample2() {
        Supplier supplier = new Supplier();
        supplier.id = 2L;
        supplier.acronym = "acronym2";
        supplier.companyName = "companyName2";
        supplier.streetAddress = "streetAddress2";
        supplier.houseNumber = "houseNumber2";
        supplier.locationName = "locationName2";
        supplier.city = "city2";
        supplier.stateProvince = "stateProvince2";
        supplier.zipPostalCode = "zipPostalCode2";
        supplier.countryRegion = "countryRegion2";
        supplier.mainEmail = "mainEmail2";
        supplier.phoneNumber1 = "phoneNumber12";
        supplier.phoneNumber2 = "phoneNumber22";
        supplier.customAttributesDetailsJSON = "customAttributesDetailsJSON2";
        return supplier;
    }

    public static Supplier getSupplierRandomSampleGenerator() {
        Supplier supplier = new Supplier();
        supplier.id = longCount.incrementAndGet();
        supplier.acronym = UUID.randomUUID().toString();
        supplier.companyName = UUID.randomUUID().toString();
        supplier.streetAddress = UUID.randomUUID().toString();
        supplier.houseNumber = UUID.randomUUID().toString();
        supplier.locationName = UUID.randomUUID().toString();
        supplier.city = UUID.randomUUID().toString();
        supplier.stateProvince = UUID.randomUUID().toString();
        supplier.zipPostalCode = UUID.randomUUID().toString();
        supplier.countryRegion = UUID.randomUUID().toString();
        supplier.mainEmail = UUID.randomUUID().toString();
        supplier.phoneNumber1 = UUID.randomUUID().toString();
        supplier.phoneNumber2 = UUID.randomUUID().toString();
        supplier.customAttributesDetailsJSON = UUID.randomUUID().toString();
        return supplier;
    }
}
