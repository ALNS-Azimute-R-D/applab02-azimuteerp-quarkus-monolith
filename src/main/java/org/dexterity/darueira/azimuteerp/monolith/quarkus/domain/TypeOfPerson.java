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
 * - TypeOfPerson
 * - Person
 * - OrganizationRole
 * - OrganizationMembership
 * - OrganizationMemberRole
 */
@Entity
@Table(name = "tb_type_person")
@Cacheable
@RegisterForReflection
public class TypeOfPerson extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 5)
    @Column(name = "code", length = 5, nullable = false)
    public String code;

    @NotNull
    @Size(max = 80)
    @Column(name = "description", length = 80, nullable = false)
    public String description;

    @OneToMany(mappedBy = "typeOfPerson")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Person> personsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfPerson)) {
            return false;
        }
        return id != null && id.equals(((TypeOfPerson) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TypeOfPerson{" + "id=" + id + ", code='" + code + "'" + ", description='" + description + "'" + "}";
    }

    public TypeOfPerson update() {
        return update(this);
    }

    public TypeOfPerson persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static TypeOfPerson update(TypeOfPerson typeOfPerson) {
        if (typeOfPerson == null) {
            throw new IllegalArgumentException("typeOfPerson can't be null");
        }
        var entity = TypeOfPerson.<TypeOfPerson>findById(typeOfPerson.id);
        if (entity != null) {
            entity.code = typeOfPerson.code;
            entity.description = typeOfPerson.description;
            entity.personsLists = typeOfPerson.personsLists;
        }
        return entity;
    }

    public static TypeOfPerson persistOrUpdate(TypeOfPerson typeOfPerson) {
        if (typeOfPerson == null) {
            throw new IllegalArgumentException("typeOfPerson can't be null");
        }
        if (typeOfPerson.id == null) {
            persist(typeOfPerson);
            return typeOfPerson;
        } else {
            return update(typeOfPerson);
        }
    }
}
