package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.InventoryTransaction} entity.
 */
@RegisterForReflection
public class InventoryTransactionDTO implements Serializable {

    public Long id;

    @NotNull
    public Long invoiceId;

    public Instant transactionCreatedDate;

    public Instant transactionModifiedDate;

    @NotNull
    public Integer quantity;

    @Size(max = 512)
    public String transactionComments;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long supplierId;
    public String supplier;
    public Long productId;
    public String product;
    public Long warehouseId;
    public String warehouse;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryTransactionDTO)) {
            return false;
        }

        return id != null && id.equals(((InventoryTransactionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "InventoryTransactionDTO{" +
            ", id=" +
            id +
            ", invoiceId=" +
            invoiceId +
            ", transactionCreatedDate='" +
            transactionCreatedDate +
            "'" +
            ", transactionModifiedDate='" +
            transactionModifiedDate +
            "'" +
            ", quantity=" +
            quantity +
            ", transactionComments='" +
            transactionComments +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", supplierId=" +
            supplierId +
            ", supplier='" +
            supplier +
            "'" +
            ", productId=" +
            productId +
            ", product='" +
            product +
            "'" +
            ", warehouseId=" +
            warehouseId +
            ", warehouse='" +
            warehouse +
            "'" +
            "}"
        );
    }
}
