package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Tenant} entity.
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

    @Size(max = 2048)
    public String mainContactPersonDetailsJSON;

    @Size(max = 2048)
    public String billingDetailsJSON;

    @Size(max = 4096)
    public String technicalEnvironmentsDetailsJSON;

    @Size(max = 4096)
    public String customAttributesDetailsJSON;

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
            ", mainContactPersonDetailsJSON='" +
            mainContactPersonDetailsJSON +
            "'" +
            ", billingDetailsJSON='" +
            billingDetailsJSON +
            "'" +
            ", technicalEnvironmentsDetailsJSON='" +
            technicalEnvironmentsDetailsJSON +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
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
