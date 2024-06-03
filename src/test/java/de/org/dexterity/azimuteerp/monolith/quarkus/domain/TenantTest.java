package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.TenantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class TenantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tenant.class);
        Tenant tenant1 = getTenantSample1();
        Tenant tenant2 = new Tenant();
        assertThat(tenant1).isNotEqualTo(tenant2);

        tenant2.id = tenant1.id;
        assertThat(tenant1).isEqualTo(tenant2);

        tenant2 = getTenantSample2();
        assertThat(tenant1).isNotEqualTo(tenant2);
    }
}
