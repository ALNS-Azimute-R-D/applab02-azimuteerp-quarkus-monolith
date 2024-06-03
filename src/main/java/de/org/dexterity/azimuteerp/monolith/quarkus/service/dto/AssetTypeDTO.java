package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetType} entity.
 */
@RegisterForReflection
public class AssetTypeDTO implements Serializable {

    public Long id;

    @Size(max = 30)
    public String acronym;

    @NotNull
    @Size(max = 100)
    public String name;

    @Size(max = 255)
    public String description;

    @Size(max = 255)
    public String handlerClazzName;

    @Lob
    public String extraDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetTypeDTO)) {
            return false;
        }

        return id != null && id.equals(((AssetTypeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "AssetTypeDTO{" +
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
            ", handlerClazzName='" +
            handlerClazzName +
            "'" +
            ", extraDetails='" +
            extraDetails +
            "'" +
            "}"
        );
    }
}
