package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Province} entity.
 */
@RegisterForReflection
public class ProvinceDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 3)
    public String acronym;

    @NotNull
    @Size(max = 40)
    public String name;

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
        if (!(o instanceof ProvinceDTO)) {
            return false;
        }

        return id != null && id.equals(((ProvinceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "ProvinceDTO{" +
            ", id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", name='" +
            name +
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
