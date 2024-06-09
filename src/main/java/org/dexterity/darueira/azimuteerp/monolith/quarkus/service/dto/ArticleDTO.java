package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.SizeOptionEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Article} entity.
 */
@RegisterForReflection
public class ArticleDTO implements Serializable {

    public Long id;

    @NotNull
    public Long inventoryProductId;

    @Size(max = 60)
    public String skuCode;

    @Size(max = 150)
    public String customName;

    @Size(max = 8192)
    public String customDescription;

    public BigDecimal priceValue;

    @NotNull
    public SizeOptionEnum itemSize;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Set<AssetCollectionDTO> assetCollections = new HashSet<>();
    public Long mainCategoryId;
    public String mainCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleDTO)) {
            return false;
        }

        return id != null && id.equals(((ArticleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "ArticleDTO{" +
            ", id=" +
            id +
            ", inventoryProductId=" +
            inventoryProductId +
            ", skuCode='" +
            skuCode +
            "'" +
            ", customName='" +
            customName +
            "'" +
            ", customDescription='" +
            customDescription +
            "'" +
            ", priceValue=" +
            priceValue +
            ", itemSize='" +
            itemSize +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", assetCollections='" +
            assetCollections +
            "'" +
            ", mainCategoryId=" +
            mainCategoryId +
            ", mainCategory='" +
            mainCategory +
            "'" +
            "}"
        );
    }
}
