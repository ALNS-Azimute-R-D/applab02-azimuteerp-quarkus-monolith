package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.config.Constants;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMemberRole} entity.
 */
@RegisterForReflection
public class OrganizationMemberRoleDTO implements Serializable {

    public Long id;

    @NotNull
    @JsonbDateFormat(value = Constants.LOCAL_DATE_FORMAT)
    public LocalDate joinedAt;

    public Long organizationMembershipId;
    public String organizationMembership;
    public Long organizationRoleId;
    public String organizationRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationMemberRoleDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationMemberRoleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationMemberRoleDTO{" +
            ", id=" +
            id +
            ", joinedAt='" +
            joinedAt +
            "'" +
            ", organizationMembershipId=" +
            organizationMembershipId +
            ", organizationMembership='" +
            organizationMembership +
            "'" +
            ", organizationRoleId=" +
            organizationRoleId +
            ", organizationRole='" +
            organizationRole +
            "'" +
            "}"
        );
    }
}
