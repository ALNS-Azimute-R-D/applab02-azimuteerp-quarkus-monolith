package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommonLocalityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CommonLocality getCommonLocalitySample1() {
        CommonLocality commonLocality = new CommonLocality();
        commonLocality.id = 1L;
        commonLocality.acronym = "acronym1";
        commonLocality.name = "name1";
        commonLocality.description = "description1";
        commonLocality.streetAddress = "streetAddress1";
        commonLocality.houseNumber = "houseNumber1";
        commonLocality.locationName = "locationName1";
        commonLocality.postalCode = "postalCode1";
        return commonLocality;
    }

    public static CommonLocality getCommonLocalitySample2() {
        CommonLocality commonLocality = new CommonLocality();
        commonLocality.id = 2L;
        commonLocality.acronym = "acronym2";
        commonLocality.name = "name2";
        commonLocality.description = "description2";
        commonLocality.streetAddress = "streetAddress2";
        commonLocality.houseNumber = "houseNumber2";
        commonLocality.locationName = "locationName2";
        commonLocality.postalCode = "postalCode2";
        return commonLocality;
    }

    public static CommonLocality getCommonLocalityRandomSampleGenerator() {
        CommonLocality commonLocality = new CommonLocality();
        commonLocality.id = longCount.incrementAndGet();
        commonLocality.acronym = UUID.randomUUID().toString();
        commonLocality.name = UUID.randomUUID().toString();
        commonLocality.description = UUID.randomUUID().toString();
        commonLocality.streetAddress = UUID.randomUUID().toString();
        commonLocality.houseNumber = UUID.randomUUID().toString();
        commonLocality.locationName = UUID.randomUUID().toString();
        commonLocality.postalCode = UUID.randomUUID().toString();
        return commonLocality;
    }
}
