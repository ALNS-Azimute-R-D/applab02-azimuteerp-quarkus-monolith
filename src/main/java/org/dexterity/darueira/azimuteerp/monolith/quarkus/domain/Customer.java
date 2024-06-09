package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.CustomerStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "fullname", length = 80, nullable = false)
    public String fullname;

    @Size(max = 2048)
    @Column(name = "custom_attributes_details_json", length = 2048)
    public String customAttributesDetailsJSON;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_status", nullable = false)
    public CustomerStatusEnum customerStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "buyer_person_id")
    @NotNull
    public Person buyerPerson;

    @ManyToOne
    @JoinColumn(name = "customer_type_id")
    public CustomerType customerType;

    @ManyToOne
    @JoinColumn(name = "district_id")
    public District district;

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Order> ordersLists = new HashSet<>();

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
            ", fullname='" +
            fullname +
            "'" +
            ", customAttributesDetailsJSON='" +
            customAttributesDetailsJSON +
            "'" +
            ", customerStatus='" +
            customerStatus +
            "'" +
            ", activationStatus='" +
            activationStatus +
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
            entity.fullname = customer.fullname;
            entity.customAttributesDetailsJSON = customer.customAttributesDetailsJSON;
            entity.customerStatus = customer.customerStatus;
            entity.activationStatus = customer.activationStatus;
            entity.buyerPerson = customer.buyerPerson;
            entity.customerType = customer.customerType;
            entity.district = customer.district;
            entity.ordersLists = customer.ordersLists;
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
