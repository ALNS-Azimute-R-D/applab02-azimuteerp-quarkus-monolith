package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetMetadata} entity.
 */
@RegisterForReflection
public class AssetMetadataDTO implements Serializable {

    public Long id;

    @Size(max = 8192)
    public String metadataDetailsJSON;

    public Long assetId;
    public String asset;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetMetadataDTO)) {
            return false;
        }

        return id != null && id.equals(((AssetMetadataDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "AssetMetadataDTO{" +
            ", id=" +
            id +
            ", metadataDetailsJSON='" +
            metadataDetailsJSON +
            "'" +
            ", assetId=" +
            assetId +
            ", asset='" +
            asset +
            "'" +
            "}"
        );
    }
}
