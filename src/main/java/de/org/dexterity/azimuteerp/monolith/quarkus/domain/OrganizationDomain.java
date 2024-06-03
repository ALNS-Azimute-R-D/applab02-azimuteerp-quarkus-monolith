package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A OrganizationDomain.
 */
@Entity
@Table(name = "tb_organization_domain")
@Cacheable
@RegisterForReflection
public class OrganizationDomain extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "domain_acronym", length = 255, nullable = false)
    public String domainAcronym;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    public String name;

    @NotNull
    @Column(name = "is_verified", nullable = false)
    public Boolean isVerified;

    @Size(max = 512)
    @Column(name = "business_handler_clazz", length = 512)
    public String businessHandlerClazz;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

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
        if (!(o instanceof OrganizationDomain)) {
            return false;
        }
        return id != null && id.equals(((OrganizationDomain) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationDomain{" +
            "id=" +
            id +
            ", domainAcronym='" +
            domainAcronym +
            "'" +
            ", name='" +
            name +
            "'" +
            ", isVerified='" +
            isVerified +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }

    public OrganizationDomain update() {
        return update(this);
    }

    public OrganizationDomain persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static OrganizationDomain update(OrganizationDomain organizationDomain) {
        if (organizationDomain == null) {
            throw new IllegalArgumentException("organizationDomain can't be null");
        }
        var entity = OrganizationDomain.<OrganizationDomain>findById(organizationDomain.id);
        if (entity != null) {
            entity.domainAcronym = organizationDomain.domainAcronym;
            entity.name = organizationDomain.name;
            entity.isVerified = organizationDomain.isVerified;
            entity.businessHandlerClazz = organizationDomain.businessHandlerClazz;
            entity.activationStatus = organizationDomain.activationStatus;
            entity.organization = organizationDomain.organization;
        }
        return entity;
    }

    public static OrganizationDomain persistOrUpdate(OrganizationDomain organizationDomain) {
        if (organizationDomain == null) {
            throw new IllegalArgumentException("organizationDomain can't be null");
        }
        if (organizationDomain.id == null) {
            persist(organizationDomain);
            return organizationDomain;
        } else {
            return update(organizationDomain);
        }
    }
}
