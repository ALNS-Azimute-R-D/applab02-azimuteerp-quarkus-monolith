package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.District} entity.
 */
@RegisterForReflection
public class DistrictDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 8)
    public String acronym;

    @NotNull
    @Size(max = 40)
    public String name;

    @Lob
    public byte[] geoPolygonArea;

    public String geoPolygonAreaContentType;

    public Long townCityId;
    public String townCity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DistrictDTO)) {
            return false;
        }

        return id != null && id.equals(((DistrictDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "DistrictDTO{" +
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
            ", townCityId=" +
            townCityId +
            ", townCity='" +
            townCity +
            "'" +
            "}"
        );
    }
}
