package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrganizationMembership.
 */
@Entity
@Table(name = "tb_organization_membership")
@Cacheable
@RegisterForReflection
public class OrganizationMembership extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "joined_at", nullable = false)
    public LocalDate joinedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    @NotNull
    public Organization organization;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    @NotNull
    public Person person;

    @OneToMany(mappedBy = "organizationMembership")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrganizationMemberRole> organizationMemberRolesLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationMembership)) {
            return false;
        }
        return id != null && id.equals(((OrganizationMembership) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "OrganizationMembership{" + "id=" + id + ", joinedAt='" + joinedAt + "'" + ", activationStatus='" + activationStatus + "'" + "}"
        );
    }

    public OrganizationMembership update() {
        return update(this);
    }

    public OrganizationMembership persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static OrganizationMembership update(OrganizationMembership organizationMembership) {
        if (organizationMembership == null) {
            throw new IllegalArgumentException("organizationMembership can't be null");
        }
        var entity = OrganizationMembership.<OrganizationMembership>findById(organizationMembership.id);
        if (entity != null) {
            entity.joinedAt = organizationMembership.joinedAt;
            entity.activationStatus = organizationMembership.activationStatus;
            entity.organization = organizationMembership.organization;
            entity.person = organizationMembership.person;
            entity.organizationMemberRolesLists = organizationMembership.organizationMemberRolesLists;
        }
        return entity;
    }

    public static OrganizationMembership persistOrUpdate(OrganizationMembership organizationMembership) {
        if (organizationMembership == null) {
            throw new IllegalArgumentException("organizationMembership can't be null");
        }
        if (organizationMembership.id == null) {
            persist(organizationMembership);
            return organizationMembership;
        } else {
            return update(organizationMembership);
        }
    }
}
