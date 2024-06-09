package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Supplier} entity.
 */
@RegisterForReflection
public class SupplierDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 50)
    public String acronym;

    @NotNull
    @Size(min = 2, max = 120)
    public String companyName;

    @NotNull
    @Size(max = 120)
    public String streetAddress;

    @Size(max = 20)
    public String houseNumber;

    @Size(max = 50)
    public String locationName;

    @Size(max = 50)
    public String city;

    @Size(max = 50)
    public String stateProvince;

    @Size(max = 15)
    public String zipPostalCode;

    @Size(max = 50)
    public String countryRegion;

    @Lob
    public byte[] pointLocation;

    public String pointLocationContentType;

    @Size(max = 120)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    public String mainEmail;

    @Size(max = 15)
    public String phoneNumber1;

    @Size(max = 15)
    public String phoneNumber2;

    @Size(max = 2048)
    public String customAttributesDetailsJSON;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long representativePersonId;
    public String representativePerson;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierDTO)) {
            return false;
        }

        return id != null && id.equals(((SupplierDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "SupplierDTO{" +
            ", id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", companyName='" +
            companyName +
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
            ", city='" +
            city +
            "'" +
            ", stateProvince='" +
            stateProvince +
            "'" +
            ", zipPostalCode='" +
            zipPostalCode +
            "'" +
            ", countryRegion='" +
            countryRegion +
            "'" +
            ", pointLocation='" +
            pointLocation +
            "'" +
            ", mainEmail='" +
            mainEmail +
            "'" +
            ", phoneNumber1='" +
            phoneNumber1 +
            "'" +
            ", phoneNumber2='" +
            phoneNumber2 +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", representativePersonId=" +
            representativePersonId +
            ", representativePerson='" +
            representativePerson +
            "'" +
            "}"
        );
    }
}
