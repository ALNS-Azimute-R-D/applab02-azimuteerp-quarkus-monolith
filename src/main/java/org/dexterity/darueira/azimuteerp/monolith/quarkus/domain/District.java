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
 * A District.
 */
@Entity
@Table(name = "tb_district")
@Cacheable
@RegisterForReflection
public class District extends PanacheEntityBase implements Serializable {

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
    @JoinColumn(name = "town_city_id")
    @NotNull
    public TownCity townCity;

    @OneToMany(mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<CommonLocality> commonLocalitiesLists = new HashSet<>();

    @OneToMany(mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Person> personsLists = new HashSet<>();

    @OneToMany(mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Customer> customersLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof District)) {
            return false;
        }
        return id != null && id.equals(((District) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "District{" +
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

    public District update() {
        return update(this);
    }

    public District persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static District update(District district) {
        if (district == null) {
            throw new IllegalArgumentException("district can't be null");
        }
        var entity = District.<District>findById(district.id);
        if (entity != null) {
            entity.acronym = district.acronym;
            entity.name = district.name;
            entity.geoPolygonArea = district.geoPolygonArea;
            entity.townCity = district.townCity;
            entity.commonLocalitiesLists = district.commonLocalitiesLists;
            entity.personsLists = district.personsLists;
            entity.customersLists = district.customersLists;
        }
        return entity;
    }

    public static District persistOrUpdate(District district) {
        if (district == null) {
            throw new IllegalArgumentException("district can't be null");
        }
        if (district.id == null) {
            persist(district);
            return district;
        } else {
            return update(district);
        }
    }
}
