package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.PaymentStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.PaymentTypeEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Payment} entity.
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
    public PaymentStatusEnum statusPayment;

    @Size(max = 2048)
    public String customAttributesDetailsJSON;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long paymentGatewayId;
    public String paymentGateway;

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
            ", statusPayment='" +
            statusPayment +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", paymentGatewayId=" +
            paymentGatewayId +
            ", paymentGateway='" +
            paymentGateway +
            "'" +
            "}"
        );
    }
}
