package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WarehouseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Warehouse getWarehouseSample1() {
        Warehouse warehouse = new Warehouse();
        warehouse.id = 1L;
        warehouse.acronym = "acronym1";
        warehouse.name = "name1";
        warehouse.streetAddress = "streetAddress1";
        warehouse.houseNumber = "houseNumber1";
        warehouse.locationName = "locationName1";
        warehouse.postalCode = "postalCode1";
        warehouse.mainEmail = "mainEmail1";
        warehouse.landPhoneNumber = "landPhoneNumber1";
        warehouse.mobilePhoneNumber = "mobilePhoneNumber1";
        warehouse.faxNumber = "faxNumber1";
        return warehouse;
    }

    public static Warehouse getWarehouseSample2() {
        Warehouse warehouse = new Warehouse();
        warehouse.id = 2L;
        warehouse.acronym = "acronym2";
        warehouse.name = "name2";
        warehouse.streetAddress = "streetAddress2";
        warehouse.houseNumber = "houseNumber2";
        warehouse.locationName = "locationName2";
        warehouse.postalCode = "postalCode2";
        warehouse.mainEmail = "mainEmail2";
        warehouse.landPhoneNumber = "landPhoneNumber2";
        warehouse.mobilePhoneNumber = "mobilePhoneNumber2";
        warehouse.faxNumber = "faxNumber2";
        return warehouse;
    }

    public static Warehouse getWarehouseRandomSampleGenerator() {
        Warehouse warehouse = new Warehouse();
        warehouse.id = longCount.incrementAndGet();
        warehouse.acronym = UUID.randomUUID().toString();
        warehouse.name = UUID.randomUUID().toString();
        warehouse.streetAddress = UUID.randomUUID().toString();
        warehouse.houseNumber = UUID.randomUUID().toString();
        warehouse.locationName = UUID.randomUUID().toString();
        warehouse.postalCode = UUID.randomUUID().toString();
        warehouse.mainEmail = UUID.randomUUID().toString();
        warehouse.landPhoneNumber = UUID.randomUUID().toString();
        warehouse.mobilePhoneNumber = UUID.randomUUID().toString();
        warehouse.faxNumber = UUID.randomUUID().toString();
        return warehouse;
    }
}
