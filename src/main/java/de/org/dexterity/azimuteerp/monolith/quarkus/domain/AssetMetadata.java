package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

/**
 * A AssetMetadata.
 */
@Entity
@Table(name = "tb_asset_metadata")
@Cacheable
@RegisterForReflection
public class AssetMetadata extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "metadata_details", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String metadataDetails;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    public Asset asset;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetMetadata)) {
            return false;
        }
        return id != null && id.equals(((AssetMetadata) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AssetMetadata{" + "id=" + id + ", metadataDetails='" + metadataDetails + "'" + "}";
    }

    public AssetMetadata update() {
        return update(this);
    }

    public AssetMetadata persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static AssetMetadata update(AssetMetadata assetMetadata) {
        if (assetMetadata == null) {
            throw new IllegalArgumentException("assetMetadata can't be null");
        }
        var entity = AssetMetadata.<AssetMetadata>findById(assetMetadata.id);
        if (entity != null) {
            entity.metadataDetails = assetMetadata.metadataDetails;
            entity.asset = assetMetadata.asset;
        }
        return entity;
    }

    public static AssetMetadata persistOrUpdate(AssetMetadata assetMetadata) {
        if (assetMetadata == null) {
            throw new IllegalArgumentException("assetMetadata can't be null");
        }
        if (assetMetadata.id == null) {
            persist(assetMetadata);
            return assetMetadata;
        } else {
            return update(assetMetadata);
        }
    }
}
