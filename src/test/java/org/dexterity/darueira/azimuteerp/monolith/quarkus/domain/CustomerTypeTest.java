package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerTypeTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerType.class);
        CustomerType customerType1 = getCustomerTypeSample1();
        CustomerType customerType2 = new CustomerType();
        assertThat(customerType1).isNotEqualTo(customerType2);

        customerType2.id = customerType1.id;
        assertThat(customerType1).isEqualTo(customerType2);

        customerType2 = getCustomerTypeSample2();
        assertThat(customerType1).isNotEqualTo(customerType2);
    }
}
