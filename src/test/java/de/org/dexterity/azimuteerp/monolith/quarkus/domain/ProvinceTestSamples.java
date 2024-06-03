package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProvinceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Province getProvinceSample1() {
        Province province = new Province();
        province.id = 1L;
        province.acronym = "acronym1";
        province.name = "name1";
        return province;
    }

    public static Province getProvinceSample2() {
        Province province = new Province();
        province.id = 2L;
        province.acronym = "acronym2";
        province.name = "name2";
        return province;
    }

    public static Province getProvinceRandomSampleGenerator() {
        Province province = new Province();
        province.id = longCount.incrementAndGet();
        province.acronym = UUID.randomUUID().toString();
        province.name = UUID.randomUUID().toString();
        return province;
    }
}
