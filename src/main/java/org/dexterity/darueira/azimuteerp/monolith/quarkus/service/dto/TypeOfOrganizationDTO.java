package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TypeOfOrganization} entity.
 */
@RegisterForReflection
public class TypeOfOrganizationDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String acronym;

    @NotNull
    @Size(max = 255)
    public String name;

    @NotNull
    @Size(max = 128)
    public String description;

    @Size(max = 512)
    public String businessHandlerClazz;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfOrganizationDTO)) {
            return false;
        }

        return id != null && id.equals(((TypeOfOrganizationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "TypeOfOrganizationDTO{" +
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
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            "}"
        );
    }
}
