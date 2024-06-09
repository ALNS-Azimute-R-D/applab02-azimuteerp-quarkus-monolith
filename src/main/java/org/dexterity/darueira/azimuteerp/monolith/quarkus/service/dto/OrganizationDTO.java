package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.OrganizationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Organization} entity.
 */
@RegisterForReflection
public class OrganizationDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String acronym;

    @NotNull
    @Size(max = 15)
    public String businessCode;

    @Size(max = 30)
    public String hierarchicalLevel;

    @NotNull
    @Size(min = 2, max = 255)
    public String name;

    @NotNull
    @Size(max = 512)
    public String description;

    @Size(max = 512)
    public String businessHandlerClazz;

    @Size(max = 2048)
    public String mainContactPersonDetailsJSON;

    @Size(max = 4096)
    public String technicalEnvironmentsDetailsJSON;

    @Size(max = 4096)
    public String customAttributesDetailsJSON;

    @NotNull
    public OrganizationStatusEnum organizationStatus;

    @NotNull
    public ActivationStatusEnum activationStatus;

    @Lob
    public byte[] logoImg;

    public String logoImgContentType;

    public Long tenantId;
    public String tenant;
    public Long typeOfOrganizationId;
    public String typeOfOrganization;
    public Long organizationParentId;
    public String organizationParent;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganizationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationDTO{" +
            ", id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", businessCode='" +
            businessCode +
            "'" +
            ", hierarchicalLevel='" +
            hierarchicalLevel +
            "'" +
            ", name='" +
            name +
            "'" +
            ", description='" +
            description +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            ", mainContactPersonDetailsJSON='" +
            mainContactPersonDetailsJSON +
            "'" +
            ", technicalEnvironmentsDetailsJSON='" +
            technicalEnvironmentsDetailsJSON +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", organizationStatus='" +
            organizationStatus +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", logoImg='" +
            logoImg +
            "'" +
            ", tenantId=" +
            tenantId +
            ", tenant='" +
            tenant +
            "'" +
            ", typeOfOrganizationId=" +
            typeOfOrganizationId +
            ", typeOfOrganization='" +
            typeOfOrganization +
            "'" +
            ", organizationParentId=" +
            organizationParentId +
            ", organizationParent='" +
            organizationParent +
            "'" +
            "}"
        );
    }
}
