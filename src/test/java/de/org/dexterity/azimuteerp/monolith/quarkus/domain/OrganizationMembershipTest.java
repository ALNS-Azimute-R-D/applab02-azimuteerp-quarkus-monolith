package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMemberRoleTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMembershipTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationMembershipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMembership.class);
        OrganizationMembership organizationMembership1 = getOrganizationMembershipSample1();
        OrganizationMembership organizationMembership2 = new OrganizationMembership();
        assertThat(organizationMembership1).isNotEqualTo(organizationMembership2);

        organizationMembership2.id = organizationMembership1.id;
        assertThat(organizationMembership1).isEqualTo(organizationMembership2);

        organizationMembership2 = getOrganizationMembershipSample2();
        assertThat(organizationMembership1).isNotEqualTo(organizationMembership2);
    }
}
