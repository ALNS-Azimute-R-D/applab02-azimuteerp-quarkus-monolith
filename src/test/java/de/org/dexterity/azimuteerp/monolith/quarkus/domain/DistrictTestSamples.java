package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DistrictTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static District getDistrictSample1() {
        District district = new District();
        district.id = 1L;
        district.acronym = "acronym1";
        district.name = "name1";
        return district;
    }

    public static District getDistrictSample2() {
        District district = new District();
        district.id = 2L;
        district.acronym = "acronym2";
        district.name = "name2";
        return district;
    }

    public static District getDistrictRandomSampleGenerator() {
        District district = new District();
        district.id = longCount.incrementAndGet();
        district.acronym = UUID.randomUUID().toString();
        district.name = UUID.randomUUID().toString();
        return district;
    }
}
