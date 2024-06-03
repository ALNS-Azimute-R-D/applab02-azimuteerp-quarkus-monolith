package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.PaymentMethod} entity.
 */
@RegisterForReflection
public class PaymentMethodDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 10)
    public String code;

    @NotNull
    @Size(max = 40)
    public String description;

    @Size(max = 512)
    public String businessHandlerClazz;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentMethodDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentMethodDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PaymentMethodDTO{" +
            ", id=" +
            id +
            ", code='" +
            code +
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
