package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BrandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Brand getBrandSample1() {
        Brand brand = new Brand();
        brand.id = 1L;
        brand.acronym = "acronym1";
        brand.name = "name1";
        return brand;
    }

    public static Brand getBrandSample2() {
        Brand brand = new Brand();
        brand.id = 2L;
        brand.acronym = "acronym2";
        brand.name = "name2";
        return brand;
    }

    public static Brand getBrandRandomSampleGenerator() {
        Brand brand = new Brand();
        brand.id = longCount.incrementAndGet();
        brand.acronym = UUID.randomUUID().toString();
        brand.name = UUID.randomUUID().toString();
        return brand;
    }
}
