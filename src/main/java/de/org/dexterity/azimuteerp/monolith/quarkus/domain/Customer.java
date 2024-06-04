package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.CustomerStatusEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

/**
 * - Category
 * - Article
 * - Order
 * - OrderItem
 */
@Entity
@Table(name = "tb_customer")
@Cacheable
@RegisterForReflection
public class Customer extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 15)
    @Column(name = "customer_business_code", length = 15, nullable = false)
    public String customerBusinessCode;

    @NotNull
    @Size(min = 2, max = 80)
    @Column(name = "name", length = 80, nullable = false)
    public String name;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String description;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false, unique = true)
    public String email;

    @Size(max = 255)
    @Column(name = "address_details", length = 255)
    public String addressDetails;

    @Size(max = 8)
    @Column(name = "zip_code", length = 8)
    public String zipCode;

    @Size(max = 1024)
    @Column(name = "keycloak_group_details", length = 1024)
    public String keycloakGroupDetails;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public CustomerStatusEnum status;

    @NotNull
    @Column(name = "active", nullable = false)
    public Boolean active;

    @ManyToOne
    @JoinColumn(name = "customer_type_id")
    public CustomerType customerType;

    @ManyToOne
    @JoinColumn(name = "district_id")
    public District district;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Customer{" +
            "id=" +
            id +
            ", customerBusinessCode='" +
            customerBusinessCode +
            "'" +
            ", name='" +
            name +
            "'" +
            ", description='" +
            description +
            "'" +
            ", email='" +
            email +
            "'" +
            ", addressDetails='" +
            addressDetails +
            "'" +
            ", zipCode='" +
            zipCode +
            "'" +
            ", keycloakGroupDetails='" +
            keycloakGroupDetails +
            "'" +
            ", status='" +
            status +
            "'" +
            ", active='" +
            active +
            "'" +
            "}"
        );
    }

    public Customer update() {
        return update(this);
    }

    public Customer persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Customer update(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer can't be null");
        }
        var entity = Customer.<Customer>findById(customer.id);
        if (entity != null) {
            entity.customerBusinessCode = customer.customerBusinessCode;
            entity.name = customer.name;
            entity.description = customer.description;
            entity.email = customer.email;
            entity.addressDetails = customer.addressDetails;
            entity.zipCode = customer.zipCode;
            entity.keycloakGroupDetails = customer.keycloakGroupDetails;
            entity.status = customer.status;
            entity.active = customer.active;
            entity.customerType = customer.customerType;
            entity.district = customer.district;
        }
        return entity;
    }

    public static Customer persistOrUpdate(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer can't be null");
        }
        if (customer.id == null) {
            persist(customer);
            return customer;
        } else {
            return update(customer);
        }
    }
}
