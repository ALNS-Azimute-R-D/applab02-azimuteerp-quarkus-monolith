package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ContinentEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Country} entity.
 */
@RegisterForReflection
public class CountryDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 3)
    public String acronym;

    @NotNull
    @Size(max = 40)
    public String name;

    @NotNull
    public ContinentEnum continent;

    @Lob
    public byte[] geoPolygonArea;

    public String geoPolygonAreaContentType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDTO)) {
            return false;
        }

        return id != null && id.equals(((CountryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "CountryDTO{" +
            ", id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", continent='" +
            continent +
            "'" +
            ", geoPolygonArea='" +
            geoPolygonArea +
            "'" +
            "}"
        );
    }
}
