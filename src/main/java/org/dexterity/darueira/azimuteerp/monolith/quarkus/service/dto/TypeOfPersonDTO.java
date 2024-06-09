package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TypeOfPerson} entity.
 */
@RegisterForReflection
public class TypeOfPersonDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 5)
    public String code;

    @NotNull
    @Size(max = 80)
    public String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfPersonDTO)) {
            return false;
        }

        return id != null && id.equals(((TypeOfPersonDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TypeOfPersonDTO{" + ", id=" + id + ", code='" + code + "'" + ", description='" + description + "'" + "}";
    }
}
