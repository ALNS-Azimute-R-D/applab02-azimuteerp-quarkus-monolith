package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.DistrictTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMembershipTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.PersonTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.PersonTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.TypeOfPersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Person.class);
        Person person1 = getPersonSample1();
        Person person2 = new Person();
        assertThat(person1).isNotEqualTo(person2);

        person2.id = person1.id;
        assertThat(person1).isEqualTo(person2);

        person2 = getPersonSample2();
        assertThat(person1).isNotEqualTo(person2);
    }
}
