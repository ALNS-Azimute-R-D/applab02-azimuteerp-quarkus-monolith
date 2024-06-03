package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMemberRoleTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationRoleTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationRole.class);
        OrganizationRole organizationRole1 = getOrganizationRoleSample1();
        OrganizationRole organizationRole2 = new OrganizationRole();
        assertThat(organizationRole1).isNotEqualTo(organizationRole2);

        organizationRole2.id = organizationRole1.id;
        assertThat(organizationRole1).isEqualTo(organizationRole2);

        organizationRole2 = getOrganizationRoleSample2();
        assertThat(organizationRole1).isNotEqualTo(organizationRole2);
    }
}
