package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.CustomerStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Customer} entity.
 */
@RegisterForReflection
public class CustomerDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 15)
    public String customerBusinessCode;

    @NotNull
    @Size(min = 2, max = 80)
    public String fullname;

    @Size(max = 2048)
    public String customAttributesDetailsJSON;

    @NotNull
    public CustomerStatusEnum customerStatus;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long buyerPersonId;
    public String buyerPerson;
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
            ", fullname='" +
            fullname +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", customerStatus='" +
            customerStatus +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", buyerPersonId=" +
            buyerPersonId +
            ", buyerPerson='" +
            buyerPerson +
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
