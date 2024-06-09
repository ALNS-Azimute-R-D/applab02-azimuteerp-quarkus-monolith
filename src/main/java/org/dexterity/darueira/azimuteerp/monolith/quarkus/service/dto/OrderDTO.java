package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.OrderStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Order} entity.
 */
@RegisterForReflection
public class OrderDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 20)
    public String businessCode;

    @NotNull
    public Instant placedDate;

    public BigDecimal totalTaxValue;

    public BigDecimal totalDueValue;

    @NotNull
    public OrderStatusEnum status;

    public Instant estimatedDeliveryDate;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long invoiceId;
    public String invoice;
    public Long customerId;
    public String customer;

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
            ", estimatedDeliveryDate='" +
            estimatedDeliveryDate +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", invoiceId=" +
            invoiceId +
            ", invoice='" +
            invoice +
            "'" +
            ", customerId=" +
            customerId +
            ", customer='" +
            customer +
            "'" +
            "}"
        );
    }
}
