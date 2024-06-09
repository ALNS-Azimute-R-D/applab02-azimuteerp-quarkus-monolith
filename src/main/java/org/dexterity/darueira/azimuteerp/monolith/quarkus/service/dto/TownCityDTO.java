package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TownCity} entity.
 */
@RegisterForReflection
public class TownCityDTO implements Serializable {

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

    public Long provinceId;
    public String province;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TownCityDTO)) {
            return false;
        }

        return id != null && id.equals(((TownCityDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "TownCityDTO{" +
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
            ", provinceId=" +
            provinceId +
            ", province='" +
            province +
            "'" +
            "}"
        );
    }
}
