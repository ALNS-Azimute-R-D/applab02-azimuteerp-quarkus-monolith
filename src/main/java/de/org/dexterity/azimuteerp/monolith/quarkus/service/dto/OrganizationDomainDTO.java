package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationDomain} entity.
 */
@RegisterForReflection
public class OrganizationDomainDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 255)
    public String domainAcronym;

    @NotNull
    @Size(min = 2, max = 255)
    public String name;

    @NotNull
    public Boolean isVerified;

    @Size(max = 512)
    public String businessHandlerClazz;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long organizationId;
    public String organization;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationDomainDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationDomainDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationDomainDTO{" +
            ", id=" +
            id +
            ", domainAcronym='" +
            domainAcronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", isVerified='" +
            isVerified +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
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
