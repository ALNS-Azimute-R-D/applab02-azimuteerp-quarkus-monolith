package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Product} entity.
 */
@RegisterForReflection
public class ProductDTO implements Serializable {

    public Long id;

    @Size(max = 25)
    public String productSKU;

    @Size(max = 50)
    public String productName;

    @Lob
    public String description;

    public BigDecimal standardCost;

    @NotNull
    public BigDecimal listPrice;

    public Integer reorderLevel;

    public Integer targetLevel;

    @Size(max = 50)
    public String quantityPerUnit;

    @NotNull
    public Boolean discontinued;

    public Integer minimumReorderQuantity;

    @Size(max = 50)
    public String suggestedCategory;

    @Lob
    public byte[] attachments;

    public String attachmentsContentType;

    @Lob
    public String supplierIds;

    public Long brandId;
    public String brand;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        return id != null && id.equals(((ProductDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "ProductDTO{" +
            ", id=" +
            id +
            ", productSKU='" +
            productSKU +
            "'" +
            ", productName='" +
            productName +
            "'" +
            ", description='" +
            description +
            "'" +
            ", standardCost=" +
            standardCost +
            ", listPrice=" +
            listPrice +
            ", reorderLevel=" +
            reorderLevel +
            ", targetLevel=" +
            targetLevel +
            ", quantityPerUnit='" +
            quantityPerUnit +
            "'" +
            ", discontinued='" +
            discontinued +
            "'" +
            ", minimumReorderQuantity=" +
            minimumReorderQuantity +
            ", suggestedCategory='" +
            suggestedCategory +
            "'" +
            ", attachments='" +
            attachments +
            "'" +
            ", supplierIds='" +
            supplierIds +
            "'" +
            ", brandId=" +
            brandId +
            ", brand='" +
            brand +
            "'" +
            "}"
        );
    }
}
