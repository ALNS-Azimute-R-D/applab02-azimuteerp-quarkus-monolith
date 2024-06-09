package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;

/**
 * A DTO for the {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetCollection} entity.
 */
@RegisterForReflection
public class AssetCollectionDTO implements Serializable {

    public Long id;

    @NotNull
    @Size(max = 512)
    public String name;

    @Size(max = 512)
    public String fullFilenamePath;

    @NotNull
    public ActivationStatusEnum activationStatus;

    public Set<AssetDTO> assets = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetCollectionDTO)) {
            return false;
        }

        return id != null && id.equals(((AssetCollectionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "AssetCollectionDTO{" +
            ", id=" +
            id +
            ", name='" +
            name +
            "'" +
            ", fullFilenamePath='" +
            fullFilenamePath +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", assets='" +
            assets +
            "'" +
            "}"
        );
    }
}
