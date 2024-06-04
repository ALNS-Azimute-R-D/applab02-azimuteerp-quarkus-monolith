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
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

/**
 * - Tenant
 * - TypeOfOrganization
 * - Organization
 * - BusinessUnit
 */
@Entity
@Table(name = "tb_tenant")
@Cacheable
@RegisterForReflection
public class Tenant extends PanacheEntityBase implements Serializable {

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
    @Size(max = 128)
    @Column(name = "name", length = 128, nullable = false)
    public String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "description", length = 255, nullable = false)
    public String description;

    @NotNull
    @Size(max = 15)
    @Column(name = "customer_business_code", length = 15, nullable = false)
    public String customerBusinessCode;

    @Size(max = 512)
    @Column(name = "business_handler_clazz", length = 512)
    public String businessHandlerClazz;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "main_contact_person_details", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String mainContactPersonDetails;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "billing_details", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String billingDetails;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "technical_environments_details", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String technicalEnvironmentsDetails;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "common_custom_attributes_details", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String commonCustomAttributesDetails;

    @Lob
    @Column(name = "logo_img")
    public byte[] logoImg;

    @Column(name = "logo_img_content_type")
    public String logoImgContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @OneToMany(mappedBy = "tenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Organization> organizationsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tenant)) {
            return false;
        }
        return id != null && id.equals(((Tenant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Tenant{" +
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
            ", customerBusinessCode='" +
            customerBusinessCode +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            ", mainContactPersonDetails='" +
            mainContactPersonDetails +
            "'" +
            ", billingDetails='" +
            billingDetails +
            "'" +
            ", technicalEnvironmentsDetails='" +
            technicalEnvironmentsDetails +
            "'" +
            ", commonCustomAttributesDetails='" +
            commonCustomAttributesDetails +
            "'" +
            ", logoImg='" +
            logoImg +
            "'" +
            ", logoImgContentType='" +
            logoImgContentType +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }

    public Tenant update() {
        return update(this);
    }

    public Tenant persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Tenant update(Tenant tenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("tenant can't be null");
        }
        var entity = Tenant.<Tenant>findById(tenant.id);
        if (entity != null) {
            entity.acronym = tenant.acronym;
            entity.name = tenant.name;
            entity.description = tenant.description;
            entity.customerBusinessCode = tenant.customerBusinessCode;
            entity.businessHandlerClazz = tenant.businessHandlerClazz;
            entity.mainContactPersonDetails = tenant.mainContactPersonDetails;
            entity.billingDetails = tenant.billingDetails;
            entity.technicalEnvironmentsDetails = tenant.technicalEnvironmentsDetails;
            entity.commonCustomAttributesDetails = tenant.commonCustomAttributesDetails;
            entity.logoImg = tenant.logoImg;
            entity.activationStatus = tenant.activationStatus;
            entity.organizationsLists = tenant.organizationsLists;
        }
        return entity;
    }

    public static Tenant persistOrUpdate(Tenant tenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("tenant can't be null");
        }
        if (tenant.id == null) {
            persist(tenant);
            return tenant;
        } else {
            return update(tenant);
        }
    }
}
