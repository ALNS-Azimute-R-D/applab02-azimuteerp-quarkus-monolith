package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.InvoiceStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Invoice} entity.
 */
@RegisterForReflection
public class InvoiceDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 15)
    public String businessCode;

    public Instant invoiceDate;

    public Instant dueDate;

    @NotNull
    @Size(max = 80)
    public String description;

    public BigDecimal taxValue;

    public BigDecimal shippingValue;

    public BigDecimal amountDueValue;

    @NotNull
    public Integer numberOfInstallmentsOriginal;

    public Integer numberOfInstallmentsPaid;

    public BigDecimal amountPaidValue;

    @NotNull
    public InvoiceStatusEnum status;

    @Size(max = 4096)
    public String customAttributesDetailsJSON;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long preferrablePaymentGatewayId;
    public String preferrablePaymentGateway;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDTO)) {
            return false;
        }

        return id != null && id.equals(((InvoiceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "InvoiceDTO{" +
            ", id=" +
            id +
            ", businessCode='" +
            businessCode +
            "'" +
            ", invoiceDate='" +
            invoiceDate +
            "'" +
            ", dueDate='" +
            dueDate +
            "'" +
            ", description='" +
            description +
            "'" +
            ", taxValue=" +
            taxValue +
            ", shippingValue=" +
            shippingValue +
            ", amountDueValue=" +
            amountDueValue +
            ", numberOfInstallmentsOriginal=" +
            numberOfInstallmentsOriginal +
            ", numberOfInstallmentsPaid=" +
            numberOfInstallmentsPaid +
            ", amountPaidValue=" +
            amountPaidValue +
            ", status='" +
            status +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", preferrablePaymentGatewayId=" +
            preferrablePaymentGatewayId +
            ", preferrablePaymentGateway='" +
            preferrablePaymentGateway +
            "'" +
            "}"
        );
    }
}
