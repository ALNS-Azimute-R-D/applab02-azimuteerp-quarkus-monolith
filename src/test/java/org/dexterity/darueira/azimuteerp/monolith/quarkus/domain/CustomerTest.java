package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerTypeTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.DistrictTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrderTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PersonTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.id = customer1.id;
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }
}
