package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerType} entity.
 */
@RegisterForReflection
public class CustomerTypeDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String name;

    @Size(max = 255)
    public String description;

    @Size(max = 255)
    public String businessHandlerClazz;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerTypeDTO)) {
            return false;
        }

        return id != null && id.equals(((CustomerTypeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "CustomerTypeDTO{" +
            ", id=" +
            id +
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
