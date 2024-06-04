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
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

/**
 * A TypeOfOrganization.
 */
@Entity
@Table(name = "tb_type_organization")
@Cacheable
@RegisterForReflection
public class TypeOfOrganization extends PanacheEntityBase implements Serializable {

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
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    public String name;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String description;

    @Size(max = 512)
    @Column(name = "business_handler_clazz", length = 512)
    public String businessHandlerClazz;

    @OneToMany(mappedBy = "typeOfOrganization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Organization> organizationsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfOrganization)) {
            return false;
        }
        return id != null && id.equals(((TypeOfOrganization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "TypeOfOrganization{" +
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
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            "}"
        );
    }

    public TypeOfOrganization update() {
        return update(this);
    }

    public TypeOfOrganization persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static TypeOfOrganization update(TypeOfOrganization typeOfOrganization) {
        if (typeOfOrganization == null) {
            throw new IllegalArgumentException("typeOfOrganization can't be null");
        }
        var entity = TypeOfOrganization.<TypeOfOrganization>findById(typeOfOrganization.id);
        if (entity != null) {
            entity.acronym = typeOfOrganization.acronym;
            entity.name = typeOfOrganization.name;
            entity.description = typeOfOrganization.description;
            entity.businessHandlerClazz = typeOfOrganization.businessHandlerClazz;
            entity.organizationsLists = typeOfOrganization.organizationsLists;
        }
        return entity;
    }

    public static TypeOfOrganization persistOrUpdate(TypeOfOrganization typeOfOrganization) {
        if (typeOfOrganization == null) {
            throw new IllegalArgumentException("typeOfOrganization can't be null");
        }
        if (typeOfOrganization.id == null) {
            persist(typeOfOrganization);
            return typeOfOrganization;
        } else {
            return update(typeOfOrganization);
        }
    }
}
