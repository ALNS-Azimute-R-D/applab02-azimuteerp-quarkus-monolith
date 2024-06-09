package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TownCity.
 */
@Entity
@Table(name = "tb_town_city")
@Cacheable
@RegisterForReflection
public class TownCity extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 8)
    @Column(name = "acronym", length = 8, nullable = false)
    public String acronym;

    @NotNull
    @Size(max = 40)
    @Column(name = "name", length = 40, nullable = false)
    public String name;

    @Lob
    @Column(name = "geo_polygon_area")
    public byte[] geoPolygonArea;

    @Column(name = "geo_polygon_area_content_type")
    public String geoPolygonAreaContentType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "province_id")
    @NotNull
    public Province province;

    @OneToMany(mappedBy = "townCity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<District> districtsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TownCity)) {
            return false;
        }
        return id != null && id.equals(((TownCity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "TownCity{" +
            "id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", geoPolygonArea='" +
            geoPolygonArea +
            "'" +
            ", geoPolygonAreaContentType='" +
            geoPolygonAreaContentType +
            "'" +
            "}"
        );
    }

    public TownCity update() {
        return update(this);
    }

    public TownCity persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static TownCity update(TownCity townCity) {
        if (townCity == null) {
            throw new IllegalArgumentException("townCity can't be null");
        }
        var entity = TownCity.<TownCity>findById(townCity.id);
        if (entity != null) {
            entity.acronym = townCity.acronym;
            entity.name = townCity.name;
            entity.geoPolygonArea = townCity.geoPolygonArea;
            entity.province = townCity.province;
            entity.districtsLists = townCity.districtsLists;
        }
        return entity;
    }

    public static TownCity persistOrUpdate(TownCity townCity) {
        if (townCity == null) {
            throw new IllegalArgumentException("townCity can't be null");
        }
        if (townCity.id == null) {
            persist(townCity);
            return townCity;
        } else {
            return update(townCity);
        }
    }
}
