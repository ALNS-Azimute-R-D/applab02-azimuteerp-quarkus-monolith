package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProvinceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProvinceAllPropertiesEquals(Province expected, Province actual) {
        assertProvinceAutoGeneratedPropertiesEquals(expected, actual);
        assertProvinceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProvinceAllUpdatablePropertiesEquals(Province expected, Province actual) {
        assertProvinceUpdatableFieldsEquals(expected, actual);
        assertProvinceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProvinceAutoGeneratedPropertiesEquals(Province expected, Province actual) {
        assertThat(expected)
            .as("Verify Province auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProvinceUpdatableFieldsEquals(Province expected, Province actual) {
        assertThat(expected)
            .as("Verify Province relevant properties")
            .satisfies(e -> assertThat(e.acronym).as("check acronym").isEqualTo(actual.acronym))
            .satisfies(e -> assertThat(e.name).as("check name").isEqualTo(actual.name))
            .satisfies(e -> assertThat(e.geoPolygonArea).as("check geoPolygonArea").isEqualTo(actual.geoPolygonArea))
            .satisfies(
                e ->
                    assertThat(e.geoPolygonAreaContentType)
                        .as("check geoPolygonArea contenty type")
                        .isEqualTo(actual.geoPolygonAreaContentType)
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProvinceUpdatableRelationshipsEquals(Province expected, Province actual) {
        assertThat(expected)
            .as("Verify Province relationships")
            .satisfies(e -> assertThat(e.country).as("check country").isEqualTo(actual.country));
    }
}
