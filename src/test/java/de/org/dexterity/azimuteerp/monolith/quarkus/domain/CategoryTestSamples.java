package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Category getCategorySample1() {
        Category category = new Category();
        category.id = 1L;
        category.acronym = "acronym1";
        category.name = "name1";
        category.description = "description1";
        category.handlerClazzName = "handlerClazzName1";
        return category;
    }

    public static Category getCategorySample2() {
        Category category = new Category();
        category.id = 2L;
        category.acronym = "acronym2";
        category.name = "name2";
        category.description = "description2";
        category.handlerClazzName = "handlerClazzName2";
        return category;
    }

    public static Category getCategoryRandomSampleGenerator() {
        Category category = new Category();
        category.id = longCount.incrementAndGet();
        category.acronym = UUID.randomUUID().toString();
        category.name = UUID.randomUUID().toString();
        category.description = UUID.randomUUID().toString();
        category.handlerClazzName = UUID.randomUUID().toString();
        return category;
    }
}
