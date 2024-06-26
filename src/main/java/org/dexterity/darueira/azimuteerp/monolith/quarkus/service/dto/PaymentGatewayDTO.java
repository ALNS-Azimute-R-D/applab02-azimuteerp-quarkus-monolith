package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentGateway} entity.
 */
@RegisterForReflection
public class PaymentGatewayDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String aliasCode;

    @NotNull
    @Size(max = 120)
    public String description;

    @Size(max = 512)
    public String businessHandlerClazz;

    @NotNull
    public ActivationStatusEnum activationStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentGatewayDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentGatewayDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PaymentGatewayDTO{" +
            ", id=" +
            id +
            ", aliasCode='" +
            aliasCode +
            "'" +
            ", description='" +
            description +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }
}
