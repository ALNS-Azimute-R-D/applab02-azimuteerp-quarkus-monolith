package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.PreferredPurposeEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.StatusAssetEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.StorageTypeEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * A Asset.
 */
@Entity
@Table(name = "tb_asset")
@Cacheable
@RegisterForReflection
public class Asset extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Column(name = "uid")
    public UUID uid;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    public String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_type_used")
    public StorageTypeEnum storageTypeUsed;

    @Size(max = 512)
    @Column(name = "full_filename_path", length = 512)
    public String fullFilenamePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public StatusAssetEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_purpose")
    public PreferredPurposeEnum preferredPurpose;

    @Lob
    @Column(name = "asset_content_as_blob")
    public byte[] assetContentAsBlob;

    @Column(name = "asset_content_as_blob_content_type")
    public String assetContentAsBlobContentType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "asset_type_id")
    @NotNull
    public AssetType assetType;

    @ManyToOne
    @JoinColumn(name = "raw_asset_proc_tmp_id")
    public RawAssetProcTmp rawAssetProcTmp;

    @OneToOne(mappedBy = "asset")
    @JsonIgnore
    public AssetMetadata assetMetadata;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset)) {
            return false;
        }
        return id != null && id.equals(((Asset) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Asset{" +
            "id=" +
            id +
            ", uid='" +
            uid +
            "'" +
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
            ", assetContentAsBlobContentType='" +
            assetContentAsBlobContentType +
            "'" +
            "}"
        );
    }

    public Asset update() {
        return update(this);
    }

    public Asset persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Asset update(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("asset can't be null");
        }
        var entity = Asset.<Asset>findById(asset.id);
        if (entity != null) {
            entity.uid = asset.uid;
            entity.name = asset.name;
            entity.storageTypeUsed = asset.storageTypeUsed;
            entity.fullFilenamePath = asset.fullFilenamePath;
            entity.status = asset.status;
            entity.preferredPurpose = asset.preferredPurpose;
            entity.assetContentAsBlob = asset.assetContentAsBlob;
            entity.assetType = asset.assetType;
            entity.rawAssetProcTmp = asset.rawAssetProcTmp;
            entity.assetMetadata = asset.assetMetadata;
        }
        return entity;
    }

    public static Asset persistOrUpdate(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("asset can't be null");
        }
        if (asset.id == null) {
            persist(asset);
            return asset;
        } else {
            return update(asset);
        }
    }
}
