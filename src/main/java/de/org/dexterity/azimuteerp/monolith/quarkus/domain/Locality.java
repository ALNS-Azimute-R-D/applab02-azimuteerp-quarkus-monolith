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
 * A Locality.
 */
@Entity
@Table(name = "tb_locality")
@Cacheable
@RegisterForReflection
public class Locality extends PanacheEntityBase implements Serializable {

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
    @Size(max = 840)
    @Column(name = "name", length = 840, nullable = false)
    public String name;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String description;

    @Lob
    @Column(name = "geo_polygon_area")
    public byte[] geoPolygonArea;

    @Column(name = "geo_polygon_area_content_type")
    public String geoPolygonAreaContentType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id")
    @NotNull
    public Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Locality)) {
            return false;
        }
        return id != null && id.equals(((Locality) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Locality{" +
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
            ", geoPolygonArea='" +
            geoPolygonArea +
            "'" +
            ", geoPolygonAreaContentType='" +
            geoPolygonAreaContentType +
            "'" +
            "}"
        );
    }

    public Locality update() {
        return update(this);
    }

    public Locality persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Locality update(Locality locality) {
        if (locality == null) {
            throw new IllegalArgumentException("locality can't be null");
        }
        var entity = Locality.<Locality>findById(locality.id);
        if (entity != null) {
            entity.acronym = locality.acronym;
            entity.name = locality.name;
            entity.description = locality.description;
            entity.geoPolygonArea = locality.geoPolygonArea;
            entity.country = locality.country;
        }
        return entity;
    }

    public static Locality persistOrUpdate(Locality locality) {
        if (locality == null) {
            throw new IllegalArgumentException("locality can't be null");
        }
        if (locality.id == null) {
            persist(locality);
            return locality;
        } else {
            return update(locality);
        }
    }
}
