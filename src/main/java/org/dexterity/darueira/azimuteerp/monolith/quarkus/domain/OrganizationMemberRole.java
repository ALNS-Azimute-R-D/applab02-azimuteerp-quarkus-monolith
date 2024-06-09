package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A OrganizationMemberRole.
 */
@Entity
@Table(name = "tb_organization_member_role")
@Cacheable
@RegisterForReflection
public class OrganizationMemberRole extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "joined_at", nullable = false)
    public LocalDate joinedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_membership_id")
    @NotNull
    public OrganizationMembership organizationMembership;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_role_id")
    @NotNull
    public OrganizationRole organizationRole;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationMemberRole)) {
            return false;
        }
        return id != null && id.equals(((OrganizationMemberRole) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrganizationMemberRole{" + "id=" + id + ", joinedAt='" + joinedAt + "'" + "}";
    }

    public OrganizationMemberRole update() {
        return update(this);
    }

    public OrganizationMemberRole persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static OrganizationMemberRole update(OrganizationMemberRole organizationMemberRole) {
        if (organizationMemberRole == null) {
            throw new IllegalArgumentException("organizationMemberRole can't be null");
        }
        var entity = OrganizationMemberRole.<OrganizationMemberRole>findById(organizationMemberRole.id);
        if (entity != null) {
            entity.joinedAt = organizationMemberRole.joinedAt;
            entity.organizationMembership = organizationMemberRole.organizationMembership;
            entity.organizationRole = organizationMemberRole.organizationRole;
        }
        return entity;
    }

    public static OrganizationMemberRole persistOrUpdate(OrganizationMemberRole organizationMemberRole) {
        if (organizationMemberRole == null) {
            throw new IllegalArgumentException("organizationMemberRole can't be null");
        }
        if (organizationMemberRole.id == null) {
            persist(organizationMemberRole);
            return organizationMemberRole;
        } else {
            return update(organizationMemberRole);
        }
    }
}
