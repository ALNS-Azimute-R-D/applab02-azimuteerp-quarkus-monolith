package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.StatusRawProcessingEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A RawAssetProcTmp.
 */
@Entity
@Table(name = "tb_raw_asset_proc_tmp")
@Cacheable
@RegisterForReflection
public class RawAssetProcTmp extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    public String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_raw_processing")
    public StatusRawProcessingEnum statusRawProcessing;

    @Size(max = 512)
    @Column(name = "full_filename_path", length = 512)
    public String fullFilenamePath;

    @Lob
    @Column(name = "asset_raw_content_as_blob")
    public byte[] assetRawContentAsBlob;

    @Column(name = "asset_raw_content_as_blob_content_type")
    public String assetRawContentAsBlobContentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "extra_details")
    public String extraDetails;

    @ManyToOne(optional = false)
    @JoinColumn(name = "asset_type_id")
    @NotNull
    public AssetType assetType;

    @OneToMany(mappedBy = "rawAssetProcTmp")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Asset> assets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RawAssetProcTmp)) {
            return false;
        }
        return id != null && id.equals(((RawAssetProcTmp) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "RawAssetProcTmp{" +
            "id=" +
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
            ", assetRawContentAsBlobContentType='" +
            assetRawContentAsBlobContentType +
            "'" +
            ", extraDetails='" +
            extraDetails +
            "'" +
            "}"
        );
    }

    public RawAssetProcTmp update() {
        return update(this);
    }

    public RawAssetProcTmp persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static RawAssetProcTmp update(RawAssetProcTmp rawAssetProcTmp) {
        if (rawAssetProcTmp == null) {
            throw new IllegalArgumentException("rawAssetProcTmp can't be null");
        }
        var entity = RawAssetProcTmp.<RawAssetProcTmp>findById(rawAssetProcTmp.id);
        if (entity != null) {
            entity.name = rawAssetProcTmp.name;
            entity.statusRawProcessing = rawAssetProcTmp.statusRawProcessing;
            entity.fullFilenamePath = rawAssetProcTmp.fullFilenamePath;
            entity.assetRawContentAsBlob = rawAssetProcTmp.assetRawContentAsBlob;
            entity.extraDetails = rawAssetProcTmp.extraDetails;
            entity.assetType = rawAssetProcTmp.assetType;
            entity.assets = rawAssetProcTmp.assets;
        }
        return entity;
    }

    public static RawAssetProcTmp persistOrUpdate(RawAssetProcTmp rawAssetProcTmp) {
        if (rawAssetProcTmp == null) {
            throw new IllegalArgumentException("rawAssetProcTmp can't be null");
        }
        if (rawAssetProcTmp.id == null) {
            persist(rawAssetProcTmp);
            return rawAssetProcTmp;
        } else {
            return update(rawAssetProcTmp);
        }
    }
}
