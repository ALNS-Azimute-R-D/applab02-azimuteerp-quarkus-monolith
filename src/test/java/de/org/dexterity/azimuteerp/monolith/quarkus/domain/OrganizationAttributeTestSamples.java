package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrganizationAttributeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrganizationAttribute getOrganizationAttributeSample1() {
        OrganizationAttribute organizationAttribute = new OrganizationAttribute();
        organizationAttribute.id = 1L;
        organizationAttribute.attributeName = "attributeName1";
        organizationAttribute.attributeValue = "attributeValue1";
        return organizationAttribute;
    }

    public static OrganizationAttribute getOrganizationAttributeSample2() {
        OrganizationAttribute organizationAttribute = new OrganizationAttribute();
        organizationAttribute.id = 2L;
        organizationAttribute.attributeName = "attributeName2";
        organizationAttribute.attributeValue = "attributeValue2";
        return organizationAttribute;
    }

    public static OrganizationAttribute getOrganizationAttributeRandomSampleGenerator() {
        OrganizationAttribute organizationAttribute = new OrganizationAttribute();
        organizationAttribute.id = longCount.incrementAndGet();
        organizationAttribute.attributeName = UUID.randomUUID().toString();
        organizationAttribute.attributeValue = UUID.randomUUID().toString();
        return organizationAttribute;
    }
}
