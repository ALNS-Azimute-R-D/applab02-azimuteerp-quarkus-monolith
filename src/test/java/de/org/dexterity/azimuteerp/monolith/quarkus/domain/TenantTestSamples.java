package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TenantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tenant getTenantSample1() {
        Tenant tenant = new Tenant();
        tenant.id = 1L;
        tenant.acronym = "acronym1";
        tenant.name = "name1";
        tenant.description = "description1";
        tenant.customerBusinessCode = "customerBusinessCode1";
        tenant.businessHandlerClazz = "businessHandlerClazz1";
        return tenant;
    }

    public static Tenant getTenantSample2() {
        Tenant tenant = new Tenant();
        tenant.id = 2L;
        tenant.acronym = "acronym2";
        tenant.name = "name2";
        tenant.description = "description2";
        tenant.customerBusinessCode = "customerBusinessCode2";
        tenant.businessHandlerClazz = "businessHandlerClazz2";
        return tenant;
    }

    public static Tenant getTenantRandomSampleGenerator() {
        Tenant tenant = new Tenant();
        tenant.id = longCount.incrementAndGet();
        tenant.acronym = UUID.randomUUID().toString();
        tenant.name = UUID.randomUUID().toString();
        tenant.description = UUID.randomUUID().toString();
        tenant.customerBusinessCode = UUID.randomUUID().toString();
        tenant.businessHandlerClazz = UUID.randomUUID().toString();
        return tenant;
    }
}
