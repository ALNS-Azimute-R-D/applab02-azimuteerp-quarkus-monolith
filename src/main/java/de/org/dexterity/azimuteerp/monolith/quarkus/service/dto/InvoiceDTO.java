package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.InvoiceStatusEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Invoice} entity.
 */
@RegisterForReflection
public class InvoiceDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 15)
    public String businessCode;

    public Long originalOrderId;

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

    @Lob
    public String extraDetails;

    public Long preferrablePaymentMethodId;
    public String preferrablePaymentMethod;

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
            ", originalOrderId=" +
            originalOrderId +
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
            ", extraDetails='" +
            extraDetails +
            "'" +
            ", preferrablePaymentMethodId=" +
            preferrablePaymentMethodId +
            ", preferrablePaymentMethod='" +
            preferrablePaymentMethod +
            "'" +
            "}"
        );
    }
}
