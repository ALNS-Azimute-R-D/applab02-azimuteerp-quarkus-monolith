package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.OrderItemStatusEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrderItem} entity.
 */
@RegisterForReflection
public class OrderItemDTO implements Serializable {

    public Long id;

    @NotNull
    @Min(value = 0)
    public Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    public BigDecimal totalPrice;

    @NotNull
    public OrderItemStatusEnum status;

    public Long articleId;
    public String article;
    public Long orderId;
    public String order;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }

        return id != null && id.equals(((OrderItemDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrderItemDTO{" +
            ", id=" +
            id +
            ", quantity=" +
            quantity +
            ", totalPrice=" +
            totalPrice +
            ", status='" +
            status +
            "'" +
            ", articleId=" +
            articleId +
            ", article='" +
            article +
            "'" +
            ", orderId=" +
            orderId +
            ", order='" +
            order +
            "'" +
            "}"
        );
    }
}
