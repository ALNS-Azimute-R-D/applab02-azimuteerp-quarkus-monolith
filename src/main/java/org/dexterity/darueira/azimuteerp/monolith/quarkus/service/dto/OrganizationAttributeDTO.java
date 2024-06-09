package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationAttribute} entity.
 */
@RegisterForReflection
public class OrganizationAttributeDTO implements Serializable {

    public Long id;

    @Size(max = 255)
    public String attributeName;

    @Size(max = 4096)
    public String attributeValue;

    public Long organizationId;
    public String organization;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationAttributeDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationAttributeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationAttributeDTO{" +
            ", id=" +
            id +
            ", attributeName='" +
            attributeName +
            "'" +
            ", attributeValue='" +
            attributeValue +
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
