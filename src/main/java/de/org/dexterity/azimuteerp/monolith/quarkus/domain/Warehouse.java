package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * - Warehouse
 * - Supplier
 * - Brand
 * - Product
 * - InventoryTransaction
 * - StockLevel
 */
@Entity
@Table(name = "tb_warehouse")
@Cacheable
@RegisterForReflection
public class Warehouse extends PanacheEntityBase implements Serializable {

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
    @Column(name = "name", length = 120, nullable = false)
    public String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    public String description;

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

    @NotNull
    @Size(max = 9)
    @Column(name = "postal_code", length = 9, nullable = false)
    public String postalCode;

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

    @OneToMany(mappedBy = "warehouse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<InventoryTransaction> inventoryTransactionsLists = new HashSet<>();

    @OneToMany(mappedBy = "warehouse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<StockLevel> stockLevelsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warehouse)) {
            return false;
        }
        return id != null && id.equals(((Warehouse) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Warehouse{" +
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
            ", streetAddress='" +
            streetAddress +
            "'" +
            ", houseNumber='" +
            houseNumber +
            "'" +
            ", locationName='" +
            locationName +
            "'" +
            ", postalCode='" +
            postalCode +
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

    public Warehouse update() {
        return update(this);
    }

    public Warehouse persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Warehouse update(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("warehouse can't be null");
        }
        var entity = Warehouse.<Warehouse>findById(warehouse.id);
        if (entity != null) {
            entity.acronym = warehouse.acronym;
            entity.name = warehouse.name;
            entity.description = warehouse.description;
            entity.streetAddress = warehouse.streetAddress;
            entity.houseNumber = warehouse.houseNumber;
            entity.locationName = warehouse.locationName;
            entity.postalCode = warehouse.postalCode;
            entity.pointLocation = warehouse.pointLocation;
            entity.mainEmail = warehouse.mainEmail;
            entity.landPhoneNumber = warehouse.landPhoneNumber;
            entity.mobilePhoneNumber = warehouse.mobilePhoneNumber;
            entity.faxNumber = warehouse.faxNumber;
            entity.extraDetails = warehouse.extraDetails;
            entity.inventoryTransactionsLists = warehouse.inventoryTransactionsLists;
            entity.stockLevelsLists = warehouse.stockLevelsLists;
        }
        return entity;
    }

    public static Warehouse persistOrUpdate(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("warehouse can't be null");
        }
        if (warehouse.id == null) {
            persist(warehouse);
            return warehouse;
        } else {
            return update(warehouse);
        }
    }
}
