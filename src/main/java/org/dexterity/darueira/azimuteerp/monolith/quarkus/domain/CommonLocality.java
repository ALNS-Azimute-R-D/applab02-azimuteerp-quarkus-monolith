package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A CommonLocality.
 */
@Entity
@Table(name = "tb_common_locality")
@Cacheable
@RegisterForReflection
public class CommonLocality extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "acronym", length = 20, nullable = false)
    public String acronym;

    @NotNull
    @Size(max = 840)
    @Column(name = "name", length = 840, nullable = false)
    public String name;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    public String description;

    @NotNull
    @Size(max = 120)
    @Column(name = "street_address", length = 120, nullable = false)
    public String streetAddress;

    @Size(max = 20)
    @Column(name = "house_number", length = 20)
    public String houseNumber;

    @Size(max = 50)
    @Column(name = "location_name", length = 50)
    public String locationName;

    @NotNull
    @Size(max = 9)
    @Column(name = "postal_code", length = 9, nullable = false)
    public String postalCode;

    @Lob
    @Column(name = "geo_polygon_area")
    public byte[] geoPolygonArea;

    @Column(name = "geo_polygon_area_content_type")
    public String geoPolygonAreaContentType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "district_id")
    @NotNull
    public District district;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonLocality)) {
            return false;
        }
        return id != null && id.equals(((CommonLocality) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "CommonLocality{" +
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
            ", streetAddress='" +
            streetAddress +
            "'" +
            ", houseNumber='" +
            houseNumber +
            "'" +
            ", locationName='" +
            locationName +
            "'" +
            ", postalCode='" +
            postalCode +
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

    public CommonLocality update() {
        return update(this);
    }

    public CommonLocality persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static CommonLocality update(CommonLocality commonLocality) {
        if (commonLocality == null) {
            throw new IllegalArgumentException("commonLocality can't be null");
        }
        var entity = CommonLocality.<CommonLocality>findById(commonLocality.id);
        if (entity != null) {
            entity.acronym = commonLocality.acronym;
            entity.name = commonLocality.name;
            entity.description = commonLocality.description;
            entity.streetAddress = commonLocality.streetAddress;
            entity.houseNumber = commonLocality.houseNumber;
            entity.locationName = commonLocality.locationName;
            entity.postalCode = commonLocality.postalCode;
            entity.geoPolygonArea = commonLocality.geoPolygonArea;
            entity.district = commonLocality.district;
        }
        return entity;
    }

    public static CommonLocality persistOrUpdate(CommonLocality commonLocality) {
        if (commonLocality == null) {
            throw new IllegalArgumentException("commonLocality can't be null");
        }
        if (commonLocality.id == null) {
            persist(commonLocality);
            return commonLocality;
        } else {
            return update(commonLocality);
        }
    }
}
