package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BusinessUnit.
 */
@Entity
@Table(name = "tb_business_unit")
@Cacheable
@RegisterForReflection
public class BusinessUnit extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "acronym", length = 20, nullable = false)
    public String acronym;

    @Size(max = 30)
    @Column(name = "hierarchical_level", length = 30)
    public String hierarchicalLevel;

    @NotNull
    @Size(min = 2, max = 120)
    @Column(name = "name", length = 120, nullable = false)
    public String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    @NotNull
    public Organization organization;

    @ManyToOne
    @JoinColumn(name = "business_unit_parent_id")
    public BusinessUnit businessUnitParent;

    @OneToMany(mappedBy = "businessUnitParent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<BusinessUnit> childrenBusinessUnitsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessUnit)) {
            return false;
        }
        return id != null && id.equals(((BusinessUnit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "BusinessUnit{" +
            "id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", hierarchicalLevel='" +
            hierarchicalLevel +
            "'" +
            ", name='" +
            name +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }

    public BusinessUnit update() {
        return update(this);
    }

    public BusinessUnit persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static BusinessUnit update(BusinessUnit businessUnit) {
        if (businessUnit == null) {
            throw new IllegalArgumentException("businessUnit can't be null");
        }
        var entity = BusinessUnit.<BusinessUnit>findById(businessUnit.id);
        if (entity != null) {
            entity.acronym = businessUnit.acronym;
            entity.hierarchicalLevel = businessUnit.hierarchicalLevel;
            entity.name = businessUnit.name;
            entity.activationStatus = businessUnit.activationStatus;
            entity.organization = businessUnit.organization;
            entity.businessUnitParent = businessUnit.businessUnitParent;
            entity.childrenBusinessUnitsLists = businessUnit.childrenBusinessUnitsLists;
        }
        return entity;
    }

    public static BusinessUnit persistOrUpdate(BusinessUnit businessUnit) {
        if (businessUnit == null) {
            throw new IllegalArgumentException("businessUnit can't be null");
        }
        if (businessUnit.id == null) {
            persist(businessUnit);
            return businessUnit;
        } else {
            return update(businessUnit);
        }
    }
}
