package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationRole} entity.
 */
@RegisterForReflection
public class OrganizationRoleDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 255)
    public String roleName;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long organizationId;
    public String organization;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationRoleDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationRoleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationRoleDTO{" +
            ", id=" +
            id +
            ", roleName='" +
            roleName +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", organizationId=" +
            organizationId +
            ", organization='" +
            organization +
            "'" +
            "}"
        );
    }
}
