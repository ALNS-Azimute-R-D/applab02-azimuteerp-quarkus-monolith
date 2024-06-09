package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.BusinessUnitTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.BusinessUnitTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessUnit.class);
        BusinessUnit businessUnit1 = getBusinessUnitSample1();
        BusinessUnit businessUnit2 = new BusinessUnit();
        assertThat(businessUnit1).isNotEqualTo(businessUnit2);

        businessUnit2.id = businessUnit1.id;
        assertThat(businessUnit1).isEqualTo(businessUnit2);

        businessUnit2 = getBusinessUnitSample2();
        assertThat(businessUnit1).isNotEqualTo(businessUnit2);
    }
}
