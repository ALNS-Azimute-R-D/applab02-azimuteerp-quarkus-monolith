package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.PreferredPurposeEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.StatusAssetEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.StorageTypeEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Asset} entity.
 */
@RegisterForReflection
public class AssetDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 512)
    public String name;

    public StorageTypeEnum storageTypeUsed;

    @Size(max = 512)
    public String fullFilenamePath;

    public StatusAssetEnum status;

    public PreferredPurposeEnum preferredPurpose;

    @Lob
    public byte[] assetContentAsBlob;

    public String assetContentAsBlobContentType;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Long assetTypeId;
    public String assetType;
    public Long rawAssetProcTmpId;
    public String rawAssetProcTmp;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDTO)) {
            return false;
        }

        return id != null && id.equals(((AssetDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "AssetDTO{" +
            ", id=" +
            id +
            ", name='" +
            name +
            "'" +
            ", storageTypeUsed='" +
            storageTypeUsed +
            "'" +
            ", fullFilenamePath='" +
            fullFilenamePath +
            "'" +
            ", status='" +
            status +
            "'" +
            ", preferredPurpose='" +
            preferredPurpose +
            "'" +
            ", assetContentAsBlob='" +
            assetContentAsBlob +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", assetTypeId=" +
            assetTypeId +
            ", assetType='" +
            assetType +
            "'" +
            ", rawAssetProcTmpId=" +
            rawAssetProcTmpId +
            ", rawAssetProcTmp='" +
            rawAssetProcTmp +
            "'" +
            "}"
        );
    }
}
