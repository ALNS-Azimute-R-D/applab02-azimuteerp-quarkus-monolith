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

/**
 * A Province.
 */
@Entity
@Table(name = "tb_province")
@Cacheable
@RegisterForReflection
public class Province extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 3)
    @Column(name = "acronym", length = 3, nullable = false)
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
    @JoinColumn(name = "country_id")
    @NotNull
    public Country country;

    @OneToMany(mappedBy = "province")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<TownCity> townCitiesLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }
        return id != null && id.equals(((Province) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Province{" +
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

    public Province update() {
        return update(this);
    }

    public Province persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Province update(Province province) {
        if (province == null) {
            throw new IllegalArgumentException("province can't be null");
        }
        var entity = Province.<Province>findById(province.id);
        if (entity != null) {
            entity.acronym = province.acronym;
            entity.name = province.name;
            entity.geoPolygonArea = province.geoPolygonArea;
            entity.country = province.country;
            entity.townCitiesLists = province.townCitiesLists;
        }
        return entity;
    }

    public static Province persistOrUpdate(Province province) {
        if (province == null) {
            throw new IllegalArgumentException("province can't be null");
        }
        if (province.id == null) {
            persist(province);
            return province;
        } else {
            return update(province);
        }
    }
}
