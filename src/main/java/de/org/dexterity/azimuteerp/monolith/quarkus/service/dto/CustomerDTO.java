package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.CustomerStatusEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Customer} entity.
 */
@RegisterForReflection
public class CustomerDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 15)
    public String customerBusinessCode;

    @NotNull
    @Size(min = 2, max = 80)
    public String name;

    @Lob
    public String description;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    public String email;

    @Size(max = 255)
    public String addressDetails;

    @Size(max = 8)
    public String zipCode;

    @Size(max = 1024)
    public String keycloakGroupDetails;

    @NotNull
    public CustomerStatusEnum status;

    @NotNull
    public Boolean active;

    public Long customerTypeId;
    public String customerType;
    public Long districtId;
    public String district;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerDTO)) {
            return false;
        }

        return id != null && id.equals(((CustomerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "CustomerDTO{" +
            ", id=" +
            id +
            ", customerBusinessCode='" +
            customerBusinessCode +
            "'" +
            ", name='" +
            name +
            "'" +
            ", description='" +
            description +
            "'" +
            ", email='" +
            email +
            "'" +
            ", addressDetails='" +
            addressDetails +
            "'" +
            ", zipCode='" +
            zipCode +
            "'" +
            ", keycloakGroupDetails='" +
            keycloakGroupDetails +
            "'" +
            ", status='" +
            status +
            "'" +
            ", active='" +
            active +
            "'" +
            ", customerTypeId=" +
            customerTypeId +
            ", customerType='" +
            customerType +
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
