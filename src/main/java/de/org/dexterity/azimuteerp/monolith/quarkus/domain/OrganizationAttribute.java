package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A OrganizationAttribute.
 */
@Entity
@Table(name = "tb_organization_attribute")
@Cacheable
@RegisterForReflection
public class OrganizationAttribute extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Size(max = 255)
    @Column(name = "attribute_name", length = 255)
    public String attributeName;

    @Size(max = 4000)
    @Column(name = "attribute_value", length = 4000)
    public String attributeValue;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    @NotNull
    public Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationAttribute)) {
            return false;
        }
        return id != null && id.equals(((OrganizationAttribute) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationAttribute{" +
            "id=" +
            id +
            ", attributeName='" +
            attributeName +
            "'" +
            ", attributeValue='" +
            attributeValue +
            "'" +
            "}"
        );
    }

    public OrganizationAttribute update() {
        return update(this);
    }

    public OrganizationAttribute persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static OrganizationAttribute update(OrganizationAttribute organizationAttribute) {
        if (organizationAttribute == null) {
            throw new IllegalArgumentException("organizationAttribute can't be null");
        }
        var entity = OrganizationAttribute.<OrganizationAttribute>findById(organizationAttribute.id);
        if (entity != null) {
            entity.attributeName = organizationAttribute.attributeName;
            entity.attributeValue = organizationAttribute.attributeValue;
            entity.organization = organizationAttribute.organization;
        }
        return entity;
    }

    public static OrganizationAttribute persistOrUpdate(OrganizationAttribute organizationAttribute) {
        if (organizationAttribute == null) {
            throw new IllegalArgumentException("organizationAttribute can't be null");
        }
        if (organizationAttribute.id == null) {
            persist(organizationAttribute);
            return organizationAttribute;
        } else {
            return update(organizationAttribute);
        }
    }
}
