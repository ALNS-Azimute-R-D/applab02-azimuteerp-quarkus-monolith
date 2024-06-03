package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Organization getOrganizationSample1() {
        Organization organization = new Organization();
        organization.id = 1L;
        organization.acronym = "acronym1";
        organization.businessCode = "businessCode1";
        organization.hierarchicalLevel = "hierarchicalLevel1";
        organization.name = "name1";
        organization.description = "description1";
        organization.businessHandlerClazz = "businessHandlerClazz1";
        return organization;
    }

    public static Organization getOrganizationSample2() {
        Organization organization = new Organization();
        organization.id = 2L;
        organization.acronym = "acronym2";
        organization.businessCode = "businessCode2";
        organization.hierarchicalLevel = "hierarchicalLevel2";
        organization.name = "name2";
        organization.description = "description2";
        organization.businessHandlerClazz = "businessHandlerClazz2";
        return organization;
    }

    public static Organization getOrganizationRandomSampleGenerator() {
        Organization organization = new Organization();
        organization.id = longCount.incrementAndGet();
        organization.acronym = UUID.randomUUID().toString();
        organization.businessCode = UUID.randomUUID().toString();
        organization.hierarchicalLevel = UUID.randomUUID().toString();
        organization.name = UUID.randomUUID().toString();
        organization.description = UUID.randomUUID().toString();
        organization.businessHandlerClazz = UUID.randomUUID().toString();
        return organization;
    }
}
