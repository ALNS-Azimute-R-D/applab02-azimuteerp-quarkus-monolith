package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.BusinessUnit} entity.
 */
@RegisterForReflection
public class BusinessUnitDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String acronym;

    @Size(max = 30)
    public String hierarchicalLevel;

    @NotNull
    @Size(min = 2, max = 120)
    public String name;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long organizationId;
    public String organization;
    public Long businessUnitParentId;
    public String businessUnitParent;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessUnitDTO)) {
            return false;
        }

        return id != null && id.equals(((BusinessUnitDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "BusinessUnitDTO{" +
            ", id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", hierarchicalLevel='" +
            hierarchicalLevel +
            "'" +
            ", name='" +
            name +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", organizationId=" +
            organizationId +
            ", organization='" +
            organization +
            "'" +
            ", businessUnitParentId=" +
            businessUnitParentId +
            ", businessUnitParent='" +
            businessUnitParent +
            "'" +
            "}"
        );
    }
}
