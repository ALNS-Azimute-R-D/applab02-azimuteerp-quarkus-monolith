package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.BusinessUnitTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationAttributeTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationDomainTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMembershipTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationRoleTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.TenantTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.TypeOfOrganizationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organization.class);
        Organization organization1 = getOrganizationSample1();
        Organization organization2 = new Organization();
        assertThat(organization1).isNotEqualTo(organization2);

        organization2.id = organization1.id;
        assertThat(organization1).isEqualTo(organization2);

        organization2 = getOrganizationSample2();
        assertThat(organization1).isNotEqualTo(organization2);
    }
}
