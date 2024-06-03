package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

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
 * - RawAssetProcTmp
 * - AssetType
 * - Asset
 * - AssetMetadata
 */
@Entity
@Table(name = "tb_type_asset")
@Cacheable
@RegisterForReflection
public class AssetType extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Size(max = 30)
    @Column(name = "acronym", length = 30)
    public String acronym;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    public String description;

    @Size(max = 255)
    @Column(name = "handler_clazz_name", length = 255)
    public String handlerClazzName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "extra_details")
    public String extraDetails;

    @OneToMany(mappedBy = "assetType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<RawAssetProcTmp> rawAssetsProcsTmps = new HashSet<>();

    @OneToMany(mappedBy = "assetType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Asset> assets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetType)) {
            return false;
        }
        return id != null && id.equals(((AssetType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "AssetType{" +
            "id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", description='" +
            description +
            "'" +
            ", handlerClazzName='" +
            handlerClazzName +
            "'" +
            ", extraDetails='" +
            extraDetails +
            "'" +
            "}"
        );
    }

    public AssetType update() {
        return update(this);
    }

    public AssetType persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static AssetType update(AssetType assetType) {
        if (assetType == null) {
            throw new IllegalArgumentException("assetType can't be null");
        }
        var entity = AssetType.<AssetType>findById(assetType.id);
        if (entity != null) {
            entity.acronym = assetType.acronym;
            entity.name = assetType.name;
            entity.description = assetType.description;
            entity.handlerClazzName = assetType.handlerClazzName;
            entity.extraDetails = assetType.extraDetails;
            entity.rawAssetsProcsTmps = assetType.rawAssetsProcsTmps;
            entity.assets = assetType.assets;
        }
        return entity;
    }

    public static AssetType persistOrUpdate(AssetType assetType) {
        if (assetType == null) {
            throw new IllegalArgumentException("assetType can't be null");
        }
        if (assetType.id == null) {
            persist(assetType);
            return assetType;
        } else {
            return update(assetType);
        }
    }
}
