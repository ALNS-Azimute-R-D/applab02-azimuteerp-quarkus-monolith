package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.PaymentStatusEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.PaymentTypeEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Payment} entity.
 */
@RegisterForReflection
public class PaymentDTO implements Serializable {

    public Long id;

    @NotNull
    public Integer installmentNumber;

    @NotNull
    public Instant paymentDueDate;

    @NotNull
    public Instant paymentPaidDate;

    @NotNull
    public BigDecimal paymentAmount;

    @NotNull
    public PaymentTypeEnum typeOfPayment;

    @NotNull
    public PaymentStatusEnum status;

    @Lob
    public String extraDetails;

    public Long paymentMethodId;
    public String paymentMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PaymentDTO{" +
            ", id=" +
            id +
            ", installmentNumber=" +
            installmentNumber +
            ", paymentDueDate='" +
            paymentDueDate +
            "'" +
            ", paymentPaidDate='" +
            paymentPaidDate +
            "'" +
            ", paymentAmount=" +
            paymentAmount +
            ", typeOfPayment='" +
            typeOfPayment +
            "'" +
            ", status='" +
            status +
            "'" +
            ", extraDetails='" +
            extraDetails +
            "'" +
            ", paymentMethodId=" +
            paymentMethodId +
            ", paymentMethod='" +
            paymentMethod +
            "'" +
            "}"
        );
    }
}
