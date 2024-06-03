package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.OrderStatusEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Order} entity.
 */
@RegisterForReflection
public class OrderDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String businessCode;

    @NotNull
    public String customerUserId;

    @NotNull
    public Instant placedDate;

    public BigDecimal totalTaxValue;

    public BigDecimal totalDueValue;

    @NotNull
    public OrderStatusEnum status;

    public Long invoiceId;

    public Instant estimatedDeliveryDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        return id != null && id.equals(((OrderDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrderDTO{" +
            ", id=" +
            id +
            ", businessCode='" +
            businessCode +
            "'" +
            ", customerUserId='" +
            customerUserId +
            "'" +
            ", placedDate='" +
            placedDate +
            "'" +
            ", totalTaxValue=" +
            totalTaxValue +
            ", totalDueValue=" +
            totalDueValue +
            ", status='" +
            status +
            "'" +
            ", invoiceId=" +
            invoiceId +
            ", estimatedDeliveryDate='" +
            estimatedDeliveryDate +
            "'" +
            "}"
        );
    }
}
