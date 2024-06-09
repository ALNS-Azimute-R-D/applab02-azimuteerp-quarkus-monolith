package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TownCityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TownCity getTownCitySample1() {
        TownCity townCity = new TownCity();
        townCity.id = 1L;
        townCity.acronym = "acronym1";
        townCity.name = "name1";
        return townCity;
    }

    public static TownCity getTownCitySample2() {
        TownCity townCity = new TownCity();
        townCity.id = 2L;
        townCity.acronym = "acronym2";
        townCity.name = "name2";
        return townCity;
    }

    public static TownCity getTownCityRandomSampleGenerator() {
        TownCity townCity = new TownCity();
        townCity.id = longCount.incrementAndGet();
        townCity.acronym = UUID.randomUUID().toString();
        townCity.name = UUID.randomUUID().toString();
        return townCity;
    }
}
