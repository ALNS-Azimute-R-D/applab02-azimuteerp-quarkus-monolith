package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationDomainTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrganizationDomain getOrganizationDomainSample1() {
        OrganizationDomain organizationDomain = new OrganizationDomain();
        organizationDomain.id = 1L;
        organizationDomain.domainAcronym = "domainAcronym1";
        organizationDomain.name = "name1";
        organizationDomain.businessHandlerClazz = "businessHandlerClazz1";
        return organizationDomain;
    }

    public static OrganizationDomain getOrganizationDomainSample2() {
        OrganizationDomain organizationDomain = new OrganizationDomain();
        organizationDomain.id = 2L;
        organizationDomain.domainAcronym = "domainAcronym2";
        organizationDomain.name = "name2";
        organizationDomain.businessHandlerClazz = "businessHandlerClazz2";
        return organizationDomain;
    }

    public static OrganizationDomain getOrganizationDomainRandomSampleGenerator() {
        OrganizationDomain organizationDomain = new OrganizationDomain();
        organizationDomain.id = longCount.incrementAndGet();
        organizationDomain.domainAcronym = UUID.randomUUID().toString();
        organizationDomain.name = UUID.randomUUID().toString();
        organizationDomain.businessHandlerClazz = UUID.randomUUID().toString();
        return organizationDomain;
    }
}
