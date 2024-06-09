package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CommonLocality} entity.
 */
@RegisterForReflection
public class CommonLocalityDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String acronym;

    @NotNull
    @Size(max = 840)
    public String name;

    @Size(max = 512)
    public String description;

    @NotNull
    @Size(max = 120)
    public String streetAddress;

    @Size(max = 20)
    public String houseNumber;

    @Size(max = 50)
    public String locationName;

    @NotNull
    @Size(max = 9)
    public String postalCode;

    @Lob
    public byte[] geoPolygonArea;

    public String geoPolygonAreaContentType;

    public Long districtId;
    public String district;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonLocalityDTO)) {
            return false;
        }

        return id != null && id.equals(((CommonLocalityDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "CommonLocalityDTO{" +
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
            ", streetAddress='" +
            streetAddress +
            "'" +
            ", houseNumber='" +
            houseNumber +
            "'" +
            ", locationName='" +
            locationName +
            "'" +
            ", postalCode='" +
            postalCode +
            "'" +
            ", geoPolygonArea='" +
            geoPolygonArea +
            "'" +
            ", districtId=" +
            districtId +
            ", district='" +
            district +
            "'" +
            "}"
        );
    }
}
