package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BusinessUnitTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BusinessUnit getBusinessUnitSample1() {
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.id = 1L;
        businessUnit.acronym = "acronym1";
        businessUnit.hierarchicalLevel = "hierarchicalLevel1";
        businessUnit.name = "name1";
        return businessUnit;
    }

    public static BusinessUnit getBusinessUnitSample2() {
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.id = 2L;
        businessUnit.acronym = "acronym2";
        businessUnit.hierarchicalLevel = "hierarchicalLevel2";
        businessUnit.name = "name2";
        return businessUnit;
    }

    public static BusinessUnit getBusinessUnitRandomSampleGenerator() {
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.id = longCount.incrementAndGet();
        businessUnit.acronym = UUID.randomUUID().toString();
        businessUnit.hierarchicalLevel = UUID.randomUUID().toString();
        businessUnit.name = UUID.randomUUID().toString();
        return businessUnit;
    }
}
