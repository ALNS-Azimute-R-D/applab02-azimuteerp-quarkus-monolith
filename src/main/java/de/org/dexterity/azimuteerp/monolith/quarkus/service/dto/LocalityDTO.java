package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Locality} entity.
 */
@RegisterForReflection
public class LocalityDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 8)
    public String acronym;

    @NotNull
    @Size(max = 840)
    public String name;

    @Lob
    public String description;

    @Lob
    public byte[] geoPolygonArea;

    public String geoPolygonAreaContentType;

    public Long countryId;
    public String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalityDTO)) {
            return false;
        }

        return id != null && id.equals(((LocalityDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "LocalityDTO{" +
            ", id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", description='" +
            description +
            "'" +
            ", geoPolygonArea='" +
            geoPolygonArea +
            "'" +
            ", countryId=" +
            countryId +
            ", country='" +
            country +
            "'" +
            "}"
        );
    }
}
