package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.OrganizationStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Organization.
 */
@Entity
@Table(name = "tb_organization")
@Cacheable
@RegisterForReflection
public class Organization extends PanacheEntityBase implements Serializable {

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
    @Size(max = 15)
    @Column(name = "business_code", length = 15, nullable = false)
    public String businessCode;

    @Size(max = 30)
    @Column(name = "hierarchical_level", length = 30)
    public String hierarchicalLevel;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    public String name;

    @NotNull
    @Size(max = 512)
    @Column(name = "description", length = 512, nullable = false)
    public String description;

    @Size(max = 512)
    @Column(name = "business_handler_clazz", length = 512)
    public String businessHandlerClazz;

    @Size(max = 2048)
    @Column(name = "main_contact_person_details_json", length = 2048)
    public String mainContactPersonDetailsJSON;

    @Size(max = 4096)
    @Column(name = "technical_environments_details_json", length = 4096)
    public String technicalEnvironmentsDetailsJSON;

    @Size(max = 4096)
    @Column(name = "custom_attributes_details_json", length = 4096)
    public String customAttributesDetailsJSON;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "organization_status", nullable = false)
    public OrganizationStatusEnum organizationStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @Lob
    @Column(name = "logo_img")
    public byte[] logoImg;

    @Column(name = "logo_img_content_type")
    public String logoImgContentType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tenant_id")
    @NotNull
    public Tenant tenant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_of_organization_id")
    @NotNull
    public TypeOfOrganization typeOfOrganization;

    @ManyToOne
    @JoinColumn(name = "organization_parent_id")
    public Organization organizationParent;

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrganizationDomain> organizationDomainsLists = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrganizationAttribute> organizationAttributesLists = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<BusinessUnit> businessUnitsLists = new HashSet<>();

    @OneToMany(mappedBy = "organizationParent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Organization> childrenOrganizationsLists = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrganizationRole> organizationRolesLists = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrganizationMembership> organizationMembershipsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Organization{" +
            "id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", businessCode='" +
            businessCode +
            "'" +
            ", hierarchicalLevel='" +
            hierarchicalLevel +
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
            ", mainContactPersonDetailsJSON='" +
            mainContactPersonDetailsJSON +
            "'" +
            ", technicalEnvironmentsDetailsJSON='" +
            technicalEnvironmentsDetailsJSON +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", organizationStatus='" +
            organizationStatus +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            ", logoImg='" +
            logoImg +
            "'" +
            ", logoImgContentType='" +
            logoImgContentType +
            "'" +
            "}"
        );
    }

    public Organization update() {
        return update(this);
    }

    public Organization persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Organization update(Organization organization) {
        if (organization == null) {
            throw new IllegalArgumentException("organization can't be null");
        }
        var entity = Organization.<Organization>findById(organization.id);
        if (entity != null) {
            entity.acronym = organization.acronym;
            entity.businessCode = organization.businessCode;
            entity.hierarchicalLevel = organization.hierarchicalLevel;
            entity.name = organization.name;
            entity.description = organization.description;
            entity.businessHandlerClazz = organization.businessHandlerClazz;
            entity.mainContactPersonDetailsJSON = organization.mainContactPersonDetailsJSON;
            entity.technicalEnvironmentsDetailsJSON = organization.technicalEnvironmentsDetailsJSON;
            entity.customAttributesDetailsJSON = organization.customAttributesDetailsJSON;
            entity.organizationStatus = organization.organizationStatus;
            entity.activationStatus = organization.activationStatus;
            entity.logoImg = organization.logoImg;
            entity.tenant = organization.tenant;
            entity.typeOfOrganization = organization.typeOfOrganization;
            entity.organizationParent = organization.organizationParent;
            entity.organizationDomainsLists = organization.organizationDomainsLists;
            entity.organizationAttributesLists = organization.organizationAttributesLists;
            entity.businessUnitsLists = organization.businessUnitsLists;
            entity.childrenOrganizationsLists = organization.childrenOrganizationsLists;
            entity.organizationRolesLists = organization.organizationRolesLists;
            entity.organizationMembershipsLists = organization.organizationMembershipsLists;
        }
        return entity;
    }

    public static Organization persistOrUpdate(Organization organization) {
        if (organization == null) {
            throw new IllegalArgumentException("organization can't be null");
        }
        if (organization.id == null) {
            persist(organization);
            return organization;
        } else {
            return update(organization);
        }
    }
}
