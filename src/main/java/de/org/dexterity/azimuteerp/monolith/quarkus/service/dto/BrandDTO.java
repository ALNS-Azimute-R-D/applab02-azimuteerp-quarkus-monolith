package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Brand} entity.
 */
@RegisterForReflection
public class BrandDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String acronym;

    @NotNull
    @Size(min = 2, max = 120)
    public String name;

    @Lob
    public String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BrandDTO)) {
            return false;
        }

        return id != null && id.equals(((BrandDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "BrandDTO{" +
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
            "}"
        );
    }
}
