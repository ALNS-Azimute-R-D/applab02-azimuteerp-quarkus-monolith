package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CountryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Country getCountrySample1() {
        Country country = new Country();
        country.id = 1L;
        country.acronym = "acronym1";
        country.name = "name1";
        return country;
    }

    public static Country getCountrySample2() {
        Country country = new Country();
        country.id = 2L;
        country.acronym = "acronym2";
        country.name = "name2";
        return country;
    }

    public static Country getCountryRandomSampleGenerator() {
        Country country = new Country();
        country.id = longCount.incrementAndGet();
        country.acronym = UUID.randomUUID().toString();
        country.name = UUID.randomUUID().toString();
        return country;
    }
}
