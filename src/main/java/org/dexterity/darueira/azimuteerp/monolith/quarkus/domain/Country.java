package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ContinentEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * - Country
 * - Province
 * - TownCity
 * - District
 * - Locality
 */
@Entity
@Table(name = "tb_country")
@Cacheable
@RegisterForReflection
public class Country extends PanacheEntityBase implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "continent", nullable = false)
    public ContinentEnum continent;

    @Lob
    @Column(name = "geo_polygon_area")
    public byte[] geoPolygonArea;

    @Column(name = "geo_polygon_area_content_type")
    public String geoPolygonAreaContentType;

    @OneToMany(mappedBy = "country")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Province> provincesLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Country{" +
            "id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", continent='" +
            continent +
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

    public Country update() {
        return update(this);
    }

    public Country persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Country update(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("country can't be null");
        }
        var entity = Country.<Country>findById(country.id);
        if (entity != null) {
            entity.acronym = country.acronym;
            entity.name = country.name;
            entity.continent = country.continent;
            entity.geoPolygonArea = country.geoPolygonArea;
            entity.provincesLists = country.provincesLists;
        }
        return entity;
    }

    public static Country persistOrUpdate(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("country can't be null");
        }
        if (country.id == null) {
            persist(country);
            return country;
        } else {
            return update(country);
        }
    }
}
