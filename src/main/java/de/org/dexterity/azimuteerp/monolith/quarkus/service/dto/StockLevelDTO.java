package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.StockLevel} entity.
 */
@RegisterForReflection
public class StockLevelDTO implements Serializable {

    public Long id;

    @NotNull
    public Instant lastModifiedDate;

    @NotNull
    public Integer ramainingQuantity;

    @Lob
    public String extraDetails;

    public Long warehouseId;
    public String warehouse;
    public Long productId;
    public String product;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockLevelDTO)) {
            return false;
        }

        return id != null && id.equals(((StockLevelDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "StockLevelDTO{" +
            ", id=" +
            id +
            ", lastModifiedDate='" +
            lastModifiedDate +
            "'" +
            ", ramainingQuantity=" +
            ramainingQuantity +
            ", extraDetails='" +
            extraDetails +
            "'" +
            ", warehouseId=" +
            warehouseId +
            ", warehouse='" +
            warehouse +
            "'" +
            ", productId=" +
            productId +
            ", product='" +
            product +
            "'" +
            "}"
        );
    }
}
