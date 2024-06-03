package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Tenant} entity.
 */
@RegisterForReflection
public class TenantDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String acronym;

    @NotNull
    @Size(max = 128)
    public String name;

    @NotNull
    @Size(max = 255)
    public String description;

    @NotNull
    @Size(max = 15)
    public String customerBusinessCode;

    @Size(max = 512)
    public String businessHandlerClazz;

    @Lob
    public String mainContactPersonDetails;

    @Lob
    public String billingDetails;

    @Lob
    public String technicalEnvironmentsDetails;

    @Lob
    public String commonCustomAttributesDetails;

    @Lob
    public byte[] logoImg;

    public String logoImgContentType;

    @NotNull
    public ActivationStatusEnum activationStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantDTO)) {
            return false;
        }

        return id != null && id.equals(((TenantDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "TenantDTO{" +
            ", id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", description='" +
            description +
            "'" +
            ", customerBusinessCode='" +
            customerBusinessCode +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            ", mainContactPersonDetails='" +
            mainContactPersonDetails +
            "'" +
            ", billingDetails='" +
            billingDetails +
            "'" +
            ", technicalEnvironmentsDetails='" +
            technicalEnvironmentsDetails +
            "'" +
            ", commonCustomAttributesDetails='" +
            commonCustomAttributesDetails +
            "'" +
            ", logoImg='" +
            logoImg +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }
}
