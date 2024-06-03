package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
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
 * A OrganizationRole.
 */
@Entity
@Table(name = "tb_organization_role")
@Cacheable
@RegisterForReflection
public class OrganizationRole extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "role_name", length = 255, nullable = false)
    public String roleName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id")
    @NotNull
    public Organization organization;

    @OneToMany(mappedBy = "organizationRole")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrganizationMemberRole> organizationMemberRolesLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationRole)) {
            return false;
        }
        return id != null && id.equals(((OrganizationRole) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrganizationRole{" + "id=" + id + ", roleName='" + roleName + "'" + ", activationStatus='" + activationStatus + "'" + "}";
    }

    public OrganizationRole update() {
        return update(this);
    }

    public OrganizationRole persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static OrganizationRole update(OrganizationRole organizationRole) {
        if (organizationRole == null) {
            throw new IllegalArgumentException("organizationRole can't be null");
        }
        var entity = OrganizationRole.<OrganizationRole>findById(organizationRole.id);
        if (entity != null) {
            entity.roleName = organizationRole.roleName;
            entity.activationStatus = organizationRole.activationStatus;
            entity.organization = organizationRole.organization;
            entity.organizationMemberRolesLists = organizationRole.organizationMemberRolesLists;
        }
        return entity;
    }

    public static OrganizationRole persistOrUpdate(OrganizationRole organizationRole) {
        if (organizationRole == null) {
            throw new IllegalArgumentException("organizationRole can't be null");
        }
        if (organizationRole.id == null) {
            persist(organizationRole);
            return organizationRole;
        } else {
            return update(organizationRole);
        }
    }
}
