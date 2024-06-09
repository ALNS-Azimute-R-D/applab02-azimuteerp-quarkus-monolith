package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Product} entity.
 */
@RegisterForReflection
public class ProductDTO implements Serializable {

    public Long id;

    @Size(max = 25)
    public String productSKU;

    @Size(max = 50)
    public String productName;

    @Size(max = 512)
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

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long brandId;
    public String brand;
    public Set<SupplierDTO> toSuppliers = new HashSet<>();

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
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", brandId=" +
            brandId +
            ", brand='" +
            brand +
            "'" +
            ", toSuppliers='" +
            toSuppliers +
            "'" +
            "}"
        );
    }
}
