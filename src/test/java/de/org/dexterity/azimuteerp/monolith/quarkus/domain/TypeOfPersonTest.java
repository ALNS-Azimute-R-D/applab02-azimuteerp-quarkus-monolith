package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.PersonTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.TypeOfPersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfPersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfPerson.class);
        TypeOfPerson typeOfPerson1 = getTypeOfPersonSample1();
        TypeOfPerson typeOfPerson2 = new TypeOfPerson();
        assertThat(typeOfPerson1).isNotEqualTo(typeOfPerson2);

        typeOfPerson2.id = typeOfPerson1.id;
        assertThat(typeOfPerson1).isEqualTo(typeOfPerson2);

        typeOfPerson2 = getTypeOfPersonSample2();
        assertThat(typeOfPerson1).isNotEqualTo(typeOfPerson2);
    }
}
