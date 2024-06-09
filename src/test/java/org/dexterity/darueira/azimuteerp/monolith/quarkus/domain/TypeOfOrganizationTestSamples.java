package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeOfOrganizationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeOfOrganization getTypeOfOrganizationSample1() {
        TypeOfOrganization typeOfOrganization = new TypeOfOrganization();
        typeOfOrganization.id = 1L;
        typeOfOrganization.acronym = "acronym1";
        typeOfOrganization.name = "name1";
        typeOfOrganization.description = "description1";
        typeOfOrganization.businessHandlerClazz = "businessHandlerClazz1";
        return typeOfOrganization;
    }

    public static TypeOfOrganization getTypeOfOrganizationSample2() {
        TypeOfOrganization typeOfOrganization = new TypeOfOrganization();
        typeOfOrganization.id = 2L;
        typeOfOrganization.acronym = "acronym2";
        typeOfOrganization.name = "name2";
        typeOfOrganization.description = "description2";
        typeOfOrganization.businessHandlerClazz = "businessHandlerClazz2";
        return typeOfOrganization;
    }

    public static TypeOfOrganization getTypeOfOrganizationRandomSampleGenerator() {
        TypeOfOrganization typeOfOrganization = new TypeOfOrganization();
        typeOfOrganization.id = longCount.incrementAndGet();
        typeOfOrganization.acronym = UUID.randomUUID().toString();
        typeOfOrganization.name = UUID.randomUUID().toString();
        typeOfOrganization.description = UUID.randomUUID().toString();
        typeOfOrganization.businessHandlerClazz = UUID.randomUUID().toString();
        return typeOfOrganization;
    }
}
