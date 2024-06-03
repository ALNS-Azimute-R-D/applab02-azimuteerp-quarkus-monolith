package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocalityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Locality getLocalitySample1() {
        Locality locality = new Locality();
        locality.id = 1L;
        locality.acronym = "acronym1";
        locality.name = "name1";
        return locality;
    }

    public static Locality getLocalitySample2() {
        Locality locality = new Locality();
        locality.id = 2L;
        locality.acronym = "acronym2";
        locality.name = "name2";
        return locality;
    }

    public static Locality getLocalityRandomSampleGenerator() {
        Locality locality = new Locality();
        locality.id = longCount.incrementAndGet();
        locality.acronym = UUID.randomUUID().toString();
        locality.name = UUID.randomUUID().toString();
        return locality;
    }
}
