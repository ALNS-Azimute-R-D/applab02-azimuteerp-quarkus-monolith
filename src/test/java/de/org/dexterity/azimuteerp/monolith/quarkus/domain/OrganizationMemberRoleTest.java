package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMemberRoleTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMembershipTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationRoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationMemberRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMemberRole.class);
        OrganizationMemberRole organizationMemberRole1 = getOrganizationMemberRoleSample1();
        OrganizationMemberRole organizationMemberRole2 = new OrganizationMemberRole();
        assertThat(organizationMemberRole1).isNotEqualTo(organizationMemberRole2);

        organizationMemberRole2.id = organizationMemberRole1.id;
        assertThat(organizationMemberRole1).isEqualTo(organizationMemberRole2);

        organizationMemberRole2 = getOrganizationMemberRoleSample2();
        assertThat(organizationMemberRole1).isNotEqualTo(organizationMemberRole2);
    }
}
