package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Supplier.
 */
@Entity
@Table(name = "tb_supplier")
@Cacheable
@RegisterForReflection
public class Supplier extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "acronym", length = 50, nullable = false)
    public String acronym;

    @NotNull
    @Size(min = 2, max = 120)
    @Column(name = "company_name", length = 120, nullable = false)
    public String companyName;

    @NotNull
    @Size(max = 120)
    @Column(name = "street_address", length = 120, nullable = false)
    public String streetAddress;

    @Size(max = 20)
    @Column(name = "house_number", length = 20)
    public String houseNumber;

    @Size(max = 50)
    @Column(name = "location_name", length = 50)
    public String locationName;

    @Size(max = 50)
    @Column(name = "city", length = 50)
    public String city;

    @Size(max = 50)
    @Column(name = "state_province", length = 50)
    public String stateProvince;

    @Size(max = 15)
    @Column(name = "zip_postal_code", length = 15)
    public String zipPostalCode;

    @Size(max = 50)
    @Column(name = "country_region", length = 50)
    public String countryRegion;

    @Lob
    @Column(name = "point_location")
    public byte[] pointLocation;

    @Column(name = "point_location_content_type")
    public String pointLocationContentType;

    @Size(max = 120)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "main_email", length = 120)
    public String mainEmail;

    @Size(max = 15)
    @Column(name = "phone_number_1", length = 15)
    public String phoneNumber1;

    @Size(max = 15)
    @Column(name = "phone_number_2", length = 15)
    public String phoneNumber2;

    @Size(max = 2048)
    @Column(name = "custom_attributes_details_json", length = 2048)
    public String customAttributesDetailsJSON;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "representative_person_id")
    @NotNull
    public Person representativePerson;

    @OneToMany(mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<InventoryTransaction> inventoryTransactionsLists = new HashSet<>();

    @ManyToMany(mappedBy = "toSuppliers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<Product> toProducts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplier)) {
            return false;
        }
        return id != null && id.equals(((Supplier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Supplier{" +
            "id=" +
            id +
            ", acronym='" +
            acronym +
            "'" +
            ", companyName='" +
            companyName +
            "'" +
            ", streetAddress='" +
            streetAddress +
            "'" +
            ", houseNumber='" +
            houseNumber +
            "'" +
            ", locationName='" +
            locationName +
            "'" +
            ", city='" +
            city +
            "'" +
            ", stateProvince='" +
            stateProvince +
            "'" +
            ", zipPostalCode='" +
            zipPostalCode +
            "'" +
            ", countryRegion='" +
            countryRegion +
            "'" +
            ", pointLocation='" +
            pointLocation +
            "'" +
            ", pointLocationContentType='" +
            pointLocationContentType +
            "'" +
            ", mainEmail='" +
            mainEmail +
            "'" +
            ", phoneNumber1='" +
            phoneNumber1 +
            "'" +
            ", phoneNumber2='" +
            phoneNumber2 +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }

    public Supplier update() {
        return update(this);
    }

    public Supplier persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Supplier update(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("supplier can't be null");
        }
        var entity = Supplier.<Supplier>findById(supplier.id);
        if (entity != null) {
            entity.acronym = supplier.acronym;
            entity.companyName = supplier.companyName;
            entity.streetAddress = supplier.streetAddress;
            entity.houseNumber = supplier.houseNumber;
            entity.locationName = supplier.locationName;
            entity.city = supplier.city;
            entity.stateProvince = supplier.stateProvince;
            entity.zipPostalCode = supplier.zipPostalCode;
            entity.countryRegion = supplier.countryRegion;
            entity.pointLocation = supplier.pointLocation;
            entity.mainEmail = supplier.mainEmail;
            entity.phoneNumber1 = supplier.phoneNumber1;
            entity.phoneNumber2 = supplier.phoneNumber2;
            entity.customAttributesDetailsJSON = supplier.customAttributesDetailsJSON;
            entity.activationStatus = supplier.activationStatus;
            entity.representativePerson = supplier.representativePerson;
            entity.inventoryTransactionsLists = supplier.inventoryTransactionsLists;
            entity.toProducts = supplier.toProducts;
        }
        return entity;
    }

    public static Supplier persistOrUpdate(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("supplier can't be null");
        }
        if (supplier.id == null) {
            persist(supplier);
            return supplier;
        } else {
            return update(supplier);
        }
    }

    public static PanacheQuery<Supplier> findAllWithEagerRelationships() {
        return find("select distinct supplier from Supplier supplier");
    }

    public static Optional<Supplier> findOneWithEagerRelationships(Long id) {
        return find("select supplier from Supplier supplier where supplier.id =?1", id).firstResultOptional();
    }
}
