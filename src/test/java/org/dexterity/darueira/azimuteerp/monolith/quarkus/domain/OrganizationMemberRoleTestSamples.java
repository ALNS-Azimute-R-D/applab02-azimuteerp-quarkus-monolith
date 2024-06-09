package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationMemberRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrganizationMemberRole getOrganizationMemberRoleSample1() {
        OrganizationMemberRole organizationMemberRole = new OrganizationMemberRole();
        organizationMemberRole.id = 1L;
        return organizationMemberRole;
    }

    public static OrganizationMemberRole getOrganizationMemberRoleSample2() {
        OrganizationMemberRole organizationMemberRole = new OrganizationMemberRole();
        organizationMemberRole.id = 2L;
        return organizationMemberRole;
    }

    public static OrganizationMemberRole getOrganizationMemberRoleRandomSampleGenerator() {
        OrganizationMemberRole organizationMemberRole = new OrganizationMemberRole();
        organizationMemberRole.id = longCount.incrementAndGet();
        return organizationMemberRole;
    }
}
