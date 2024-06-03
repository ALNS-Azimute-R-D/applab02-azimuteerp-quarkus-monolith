package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.config.Constants;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationMembership} entity.
 */
@RegisterForReflection
public class OrganizationMembershipDTO implements Serializable {

    public Long id;

    @NotNull
    @JsonbDateFormat(value = Constants.LOCAL_DATE_FORMAT)
    public LocalDate joinedAt;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long organizationId;
    public String organization;
    public Long personId;
    public String person;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationMembershipDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationMembershipDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationMembershipDTO{" +
            ", id=" +
            id +
            ", joinedAt='" +
            joinedAt +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", organizationId=" +
            organizationId +
            ", organization='" +
            organization +
            "'" +
            ", personId=" +
            personId +
            ", person='" +
            person +
            "'" +
            "}"
        );
    }
}
