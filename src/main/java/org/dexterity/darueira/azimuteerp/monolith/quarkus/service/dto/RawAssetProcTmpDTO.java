package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.StatusRawProcessingEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.RawAssetProcTmp} entity.
 */
@RegisterForReflection
public class RawAssetProcTmpDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 255)
    public String name;

    public StatusRawProcessingEnum statusRawProcessing;

    @Size(max = 512)
    public String fullFilenamePath;

    @Lob
    public byte[] assetRawContentAsBlob;

    public String assetRawContentAsBlobContentType;

    @Size(max = 4096)
    public String customAttributesDetailsJSON;

    public Long assetTypeId;
    public String assetType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RawAssetProcTmpDTO)) {
            return false;
        }

        return id != null && id.equals(((RawAssetProcTmpDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "RawAssetProcTmpDTO{" +
            ", id=" +
            id +
            ", name='" +
            name +
            "'" +
            ", statusRawProcessing='" +
            statusRawProcessing +
            "'" +
            ", fullFilenamePath='" +
            fullFilenamePath +
            "'" +
            ", assetRawContentAsBlob='" +
            assetRawContentAsBlob +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", assetTypeId=" +
            assetTypeId +
            ", assetType='" +
            assetType +
            "'" +
            "}"
        );
    }
}
