package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.OrderStatusEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "order")
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
    @Column(name = "customer_user_id", nullable = false)
    public String customerUserId;

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

    @Column(name = "invoice_id")
    public Long invoiceId;

    @Column(name = "estimated_delivery_date")
    public Instant estimatedDeliveryDate;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrderItem> ordersItemsLists = new HashSet<>();

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
            ", customerUserId='" +
            customerUserId +
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
            ", invoiceId=" +
            invoiceId +
            ", estimatedDeliveryDate='" +
            estimatedDeliveryDate +
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
            entity.customerUserId = order.customerUserId;
            entity.placedDate = order.placedDate;
            entity.totalTaxValue = order.totalTaxValue;
            entity.totalDueValue = order.totalDueValue;
            entity.status = order.status;
            entity.invoiceId = order.invoiceId;
            entity.estimatedDeliveryDate = order.estimatedDeliveryDate;
            entity.ordersItemsLists = order.ordersItemsLists;
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
