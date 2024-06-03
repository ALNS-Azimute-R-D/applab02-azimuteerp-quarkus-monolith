package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.OrderItemStatusEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
@Cacheable
@RegisterForReflection
public class OrderItem extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    public Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
    public BigDecimal totalPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public OrderItemStatusEnum status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "article_id")
    @NotNull
    public Article article;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    @NotNull
    public Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrderItem{" + "id=" + id + ", quantity=" + quantity + ", totalPrice=" + totalPrice + ", status='" + status + "'" + "}";
    }

    public OrderItem update() {
        return update(this);
    }

    public OrderItem persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static OrderItem update(OrderItem orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("orderItem can't be null");
        }
        var entity = OrderItem.<OrderItem>findById(orderItem.id);
        if (entity != null) {
            entity.quantity = orderItem.quantity;
            entity.totalPrice = orderItem.totalPrice;
            entity.status = orderItem.status;
            entity.article = orderItem.article;
            entity.order = orderItem.order;
        }
        return entity;
    }

    public static OrderItem persistOrUpdate(OrderItem orderItem) {
        if (orderItem == null) {
            throw new IllegalArgumentException("orderItem can't be null");
        }
        if (orderItem.id == null) {
            persist(orderItem);
            return orderItem;
        } else {
            return update(orderItem);
        }
    }
}
