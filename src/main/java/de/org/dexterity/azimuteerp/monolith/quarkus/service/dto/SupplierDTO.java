package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Supplier} entity.
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

    @Size(max = 50)
    public String representativeLastName;

    @Size(max = 50)
    public String representativeFirstName;

    @Size(max = 50)
    public String jobTitle;

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
    public String webPage;

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

    @Lob
    public String extraDetails;

    public Set<ProductDTO> productsLists = new HashSet<>();

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
            ", representativeLastName='" +
            representativeLastName +
            "'" +
            ", representativeFirstName='" +
            representativeFirstName +
            "'" +
            ", jobTitle='" +
            jobTitle +
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
            ", webPage='" +
            webPage +
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
            ", extraDetails='" +
            extraDetails +
            "'" +
            ", productsLists='" +
            productsLists +
            "'" +
            "}"
        );
    }
}
