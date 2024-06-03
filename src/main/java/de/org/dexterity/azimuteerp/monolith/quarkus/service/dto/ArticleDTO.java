package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.SizeOptionEnum;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Article} entity.
 */
@RegisterForReflection
public class ArticleDTO implements Serializable {

    public Long id;

    @NotNull
    public Long inventoryProductId;

    @Size(max = 150)
    public String customName;

    @Lob
    public String customDescription;

    public BigDecimal priceValue;

    @NotNull
    public SizeOptionEnum itemSize;

    @Size(max = 255)
    public String assetsCollectionUUID;

    public Boolean isEnabled;

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
            ", assetsCollectionUUID='" +
            assetsCollectionUUID +
            "'" +
            ", isEnabled='" +
            isEnabled +
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
