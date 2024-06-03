package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrganizationRole getOrganizationRoleSample1() {
        OrganizationRole organizationRole = new OrganizationRole();
        organizationRole.id = 1L;
        organizationRole.roleName = "roleName1";
        return organizationRole;
    }

    public static OrganizationRole getOrganizationRoleSample2() {
        OrganizationRole organizationRole = new OrganizationRole();
        organizationRole.id = 2L;
        organizationRole.roleName = "roleName2";
        return organizationRole;
    }

    public static OrganizationRole getOrganizationRoleRandomSampleGenerator() {
        OrganizationRole organizationRole = new OrganizationRole();
        organizationRole.id = longCount.incrementAndGet();
        organizationRole.roleName = UUID.randomUUID().toString();
        return organizationRole;
    }
}
