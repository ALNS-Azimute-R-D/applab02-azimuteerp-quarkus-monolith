package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Warehouse} entity.
 */
@RegisterForReflection
public class WarehouseDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 50)
    public String acronym;

    @NotNull
    @Size(min = 2, max = 120)
    public String name;

    @Size(max = 1024)
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
    public byte[] pointLocation;

    public String pointLocationContentType;

    @Size(max = 120)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    public String mainEmail;

    @Size(max = 15)
    public String landPhoneNumber;

    @Size(max = 15)
    public String mobilePhoneNumber;

    @Size(max = 15)
    public String faxNumber;

    @Size(max = 2048)
    public String customAttributesDetailsJSON;

    @NotNull
    public ActivationStatusEnum activationStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarehouseDTO)) {
            return false;
        }

        return id != null && id.equals(((WarehouseDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "WarehouseDTO{" +
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
            ", pointLocation='" +
            pointLocation +
            "'" +
            ", mainEmail='" +
            mainEmail +
            "'" +
            ", landPhoneNumber='" +
            landPhoneNumber +
            "'" +
            ", mobilePhoneNumber='" +
            mobilePhoneNumber +
            "'" +
            ", faxNumber='" +
            faxNumber +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }
}
