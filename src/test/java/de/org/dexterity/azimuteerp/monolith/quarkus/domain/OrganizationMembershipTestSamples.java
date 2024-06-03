package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationMembershipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrganizationMembership getOrganizationMembershipSample1() {
        OrganizationMembership organizationMembership = new OrganizationMembership();
        organizationMembership.id = 1L;
        return organizationMembership;
    }

    public static OrganizationMembership getOrganizationMembershipSample2() {
        OrganizationMembership organizationMembership = new OrganizationMembership();
        organizationMembership.id = 2L;
        return organizationMembership;
    }

    public static OrganizationMembership getOrganizationMembershipRandomSampleGenerator() {
        OrganizationMembership organizationMembership = new OrganizationMembership();
        organizationMembership.id = longCount.incrementAndGet();
        return organizationMembership;
    }
}
