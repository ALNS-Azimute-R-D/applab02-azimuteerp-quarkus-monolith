package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.PreferredPurposeEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.StatusAssetEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.StorageTypeEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @NotNull
    @Size(max = 512)
    @Column(name = "name", length = 512, nullable = false)
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

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

    @ManyToMany(mappedBy = "assets")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<AssetCollection> assetCollections = new HashSet<>();

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
            ", activationStatus='" +
            activationStatus +
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
            entity.name = asset.name;
            entity.storageTypeUsed = asset.storageTypeUsed;
            entity.fullFilenamePath = asset.fullFilenamePath;
            entity.status = asset.status;
            entity.preferredPurpose = asset.preferredPurpose;
            entity.assetContentAsBlob = asset.assetContentAsBlob;
            entity.activationStatus = asset.activationStatus;
            entity.assetType = asset.assetType;
            entity.rawAssetProcTmp = asset.rawAssetProcTmp;
            entity.assetMetadata = asset.assetMetadata;
            entity.assetCollections = asset.assetCollections;
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

    public static PanacheQuery<Asset> findAllWithEagerRelationships() {
        return find("select distinct asset from Asset asset");
    }

    public static Optional<Asset> findOneWithEagerRelationships(Long id) {
        return find("select asset from Asset asset where asset.id =?1", id).firstResultOptional();
    }
}
