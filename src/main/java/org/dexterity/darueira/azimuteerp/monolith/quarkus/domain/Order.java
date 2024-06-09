package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.OrderStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "tb_order")
@Cacheable
@RegisterForReflection
public class Order extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "business_code", length = 20, nullable = false)
    public String businessCode;

    @NotNull
    @Column(name = "placed_date", nullable = false)
    public Instant placedDate;

    @Column(name = "total_tax_value", precision = 21, scale = 2)
    public BigDecimal totalTaxValue;

    @Column(name = "total_due_value", precision = 21, scale = 2)
    public BigDecimal totalDueValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public OrderStatusEnum status;

    @Column(name = "estimated_delivery_date")
    public Instant estimatedDeliveryDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status", nullable = false)
    public ActivationStatusEnum activationStatus;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrderItem> orderItemsLists = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    public Invoice invoice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    @NotNull
    public Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Order{" +
            "id=" +
            id +
            ", businessCode='" +
            businessCode +
            "'" +
            ", placedDate='" +
            placedDate +
            "'" +
            ", totalTaxValue=" +
            totalTaxValue +
            ", totalDueValue=" +
            totalDueValue +
            ", status='" +
            status +
            "'" +
            ", estimatedDeliveryDate='" +
            estimatedDeliveryDate +
            "'" +
            ", activationStatus='" +
            activationStatus +
            "'" +
            "}"
        );
    }

    public Order update() {
        return update(this);
    }

    public Order persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Order update(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order can't be null");
        }
        var entity = Order.<Order>findById(order.id);
        if (entity != null) {
            entity.businessCode = order.businessCode;
            entity.placedDate = order.placedDate;
            entity.totalTaxValue = order.totalTaxValue;
            entity.totalDueValue = order.totalDueValue;
            entity.status = order.status;
            entity.estimatedDeliveryDate = order.estimatedDeliveryDate;
            entity.activationStatus = order.activationStatus;
            entity.orderItemsLists = order.orderItemsLists;
            entity.invoice = order.invoice;
            entity.customer = order.customer;
        }
        return entity;
    }

    public static Order persistOrUpdate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order can't be null");
        }
        if (order.id == null) {
            persist(order);
            return order;
        } else {
            return update(order);
        }
    }
}
