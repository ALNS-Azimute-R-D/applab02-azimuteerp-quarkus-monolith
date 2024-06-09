package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

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
 * A CustomerType.
 */
@Entity
@Table(name = "tb_type_customer")
@Cacheable
@RegisterForReflection
public class CustomerType extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "name", length = 20, nullable = false)
    public String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    public String description;

    @Size(max = 255)
    @Column(name = "business_handler_clazz", length = 255)
    public String businessHandlerClazz;

    @OneToMany(mappedBy = "customerType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<Customer> customersLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerType)) {
            return false;
        }
        return id != null && id.equals(((CustomerType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "CustomerType{" +
            "id=" +
            id +
            ", name='" +
            name +
            "'" +
            ", description='" +
            description +
            "'" +
            ", businessHandlerClazz='" +
            businessHandlerClazz +
            "'" +
            "}"
        );
    }

    public CustomerType update() {
        return update(this);
    }

    public CustomerType persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static CustomerType update(CustomerType customerType) {
        if (customerType == null) {
            throw new IllegalArgumentException("customerType can't be null");
        }
        var entity = CustomerType.<CustomerType>findById(customerType.id);
        if (entity != null) {
            entity.name = customerType.name;
            entity.description = customerType.description;
            entity.businessHandlerClazz = customerType.businessHandlerClazz;
            entity.customersLists = customerType.customersLists;
        }
        return entity;
    }

    public static CustomerType persistOrUpdate(CustomerType customerType) {
        if (customerType == null) {
            throw new IllegalArgumentException("customerType can't be null");
        }
        if (customerType.id == null) {
            persist(customerType);
            return customerType;
        } else {
            return update(customerType);
        }
    }
}
