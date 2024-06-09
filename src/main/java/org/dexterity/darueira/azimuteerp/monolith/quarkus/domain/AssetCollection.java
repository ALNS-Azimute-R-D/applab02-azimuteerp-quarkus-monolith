package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssetCollection.
 */
@Entity
@Table(name = "tb_asset_collection")
@Cacheable
@RegisterForReflection
public class AssetCollection extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 512)
    @Column(name = "name", length = 512, nullable = false)
    public String name;

    @Size(max = 512)
    @Column(name = "full_filename_path", length = 512)
    public String fullFilenamePath;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_tb_asset_collection__asset",
        joinColumns = @JoinColumn(name = "tb_asset_collection_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "asset_id", referencedColumnName = "id")
    )
    @JsonbTransient
    public Set<Asset> assets = new HashSet<>();

    @ManyToMany(mappedBy = "assetCollections")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<Article> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetCollection)) {
            return false;
        }
        return id != null && id.equals(((AssetCollection) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "AssetCollection{" +
            "id=" +
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
            "}"
        );
    }

    public AssetCollection update() {
        return update(this);
    }

    public AssetCollection persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static AssetCollection update(AssetCollection assetCollection) {
        if (assetCollection == null) {
            throw new IllegalArgumentException("assetCollection can't be null");
        }
        var entity = AssetCollection.<AssetCollection>findById(assetCollection.id);
        if (entity != null) {
            entity.name = assetCollection.name;
            entity.fullFilenamePath = assetCollection.fullFilenamePath;
            entity.activationStatus = assetCollection.activationStatus;
            entity.assets = assetCollection.assets;
            entity.articles = assetCollection.articles;
        }
        return entity;
    }

    public static AssetCollection persistOrUpdate(AssetCollection assetCollection) {
        if (assetCollection == null) {
            throw new IllegalArgumentException("assetCollection can't be null");
        }
        if (assetCollection.id == null) {
            persist(assetCollection);
            return assetCollection;
        } else {
            return update(assetCollection);
        }
    }

    public static PanacheQuery<AssetCollection> findAllWithEagerRelationships() {
        return find("select distinct assetCollection from AssetCollection assetCollection left join fetch assetCollection.assets");
    }

    public static Optional<AssetCollection> findOneWithEagerRelationships(Long id) {
        return find(
            "select assetCollection from AssetCollection assetCollection left join fetch assetCollection.assets where assetCollection.id =?1",
            id
        ).firstResultOptional();
    }
}
