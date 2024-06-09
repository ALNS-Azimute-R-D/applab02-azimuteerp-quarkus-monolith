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
 * A Brand.
 */
@Entity
@Table(name = "tb_brand")
@Cacheable
@RegisterForReflection
public class Brand extends PanacheEntityBase implements Serializable {

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
    @Size(min = 2, max = 120)
    @Column(name = "name", length = 120, nullable = false)
    public String name;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    public String description;

    @Lob
    @Column(name = "logo_brand")
    public byte[] logoBrand;

    @Column(name = "logo_brand_content_type")
    public String logoBrandContentType;

    @OneToMany(mappedBy = "brand")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Product> productsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Brand)) {
            return false;
        }
        return id != null && id.equals(((Brand) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Brand{" +
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
            ", logoBrand='" +
            logoBrand +
            "'" +
            ", logoBrandContentType='" +
            logoBrandContentType +
            "'" +
            "}"
        );
    }

    public Brand update() {
        return update(this);
    }

    public Brand persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Brand update(Brand brand) {
        if (brand == null) {
            throw new IllegalArgumentException("brand can't be null");
        }
        var entity = Brand.<Brand>findById(brand.id);
        if (entity != null) {
            entity.acronym = brand.acronym;
            entity.name = brand.name;
            entity.description = brand.description;
            entity.logoBrand = brand.logoBrand;
            entity.productsLists = brand.productsLists;
        }
        return entity;
    }

    public static Brand persistOrUpdate(Brand brand) {
        if (brand == null) {
            throw new IllegalArgumentException("brand can't be null");
        }
        if (brand.id == null) {
            persist(brand);
            return brand;
        } else {
            return update(brand);
        }
    }
}
