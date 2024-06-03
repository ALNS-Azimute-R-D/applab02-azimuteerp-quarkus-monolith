package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

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

    @Size(max = 50)
    @Column(name = "representative_last_name", length = 50)
    public String representativeLastName;

    @Size(max = 50)
    @Column(name = "representative_first_name", length = 50)
    public String representativeFirstName;

    @Size(max = 50)
    @Column(name = "job_title", length = 50)
    public String jobTitle;

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
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "web_page")
    public String webPage;

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
    @Column(name = "land_phone_number", length = 15)
    public String landPhoneNumber;

    @Size(max = 15)
    @Column(name = "mobile_phone_number", length = 15)
    public String mobilePhoneNumber;

    @Size(max = 15)
    @Column(name = "fax_number", length = 15)
    public String faxNumber;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "extra_details")
    public String extraDetails;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_tb_supplier__products_list",
        joinColumns = @JoinColumn(name = "tb_supplier_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "products_list_id", referencedColumnName = "id")
    )
    @JsonbTransient
    public Set<Product> productsLists = new HashSet<>();

    @OneToMany(mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<InventoryTransaction> inventoryTransactionsLists = new HashSet<>();

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
            ", representativeLastName='" +
            representativeLastName +
            "'" +
            ", representativeFirstName='" +
            representativeFirstName +
            "'" +
            ", jobTitle='" +
            jobTitle +
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
            ", webPage='" +
            webPage +
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
            ", landPhoneNumber='" +
            landPhoneNumber +
            "'" +
            ", mobilePhoneNumber='" +
            mobilePhoneNumber +
            "'" +
            ", faxNumber='" +
            faxNumber +
            "'" +
            ", extraDetails='" +
            extraDetails +
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
            entity.representativeLastName = supplier.representativeLastName;
            entity.representativeFirstName = supplier.representativeFirstName;
            entity.jobTitle = supplier.jobTitle;
            entity.streetAddress = supplier.streetAddress;
            entity.houseNumber = supplier.houseNumber;
            entity.locationName = supplier.locationName;
            entity.city = supplier.city;
            entity.stateProvince = supplier.stateProvince;
            entity.zipPostalCode = supplier.zipPostalCode;
            entity.countryRegion = supplier.countryRegion;
            entity.webPage = supplier.webPage;
            entity.pointLocation = supplier.pointLocation;
            entity.mainEmail = supplier.mainEmail;
            entity.landPhoneNumber = supplier.landPhoneNumber;
            entity.mobilePhoneNumber = supplier.mobilePhoneNumber;
            entity.faxNumber = supplier.faxNumber;
            entity.extraDetails = supplier.extraDetails;
            entity.productsLists = supplier.productsLists;
            entity.inventoryTransactionsLists = supplier.inventoryTransactionsLists;
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
        return find("select distinct supplier from Supplier supplier left join fetch supplier.productsLists");
    }

    public static Optional<Supplier> findOneWithEagerRelationships(Long id) {
        return find(
            "select supplier from Supplier supplier left join fetch supplier.productsLists where supplier.id =?1",
            id
        ).firstResultOptional();
    }
}
