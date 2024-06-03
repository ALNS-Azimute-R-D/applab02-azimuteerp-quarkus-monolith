package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Type;

/**
 * A StockLevel.
 */
@Entity
@Table(name = "tb_stock_level")
@Cacheable
@RegisterForReflection
public class StockLevel extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "last_modified_date", nullable = false)
    public Instant lastModifiedDate;

    @NotNull
    @Column(name = "ramaining_quantity", nullable = false)
    public Integer ramainingQuantity;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "extra_details")
    public String extraDetails;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_id")
    @NotNull
    public Warehouse warehouse;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    @NotNull
    public Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockLevel)) {
            return false;
        }
        return id != null && id.equals(((StockLevel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "StockLevel{" +
            "id=" +
            id +
            ", lastModifiedDate='" +
            lastModifiedDate +
            "'" +
            ", ramainingQuantity=" +
            ramainingQuantity +
            ", extraDetails='" +
            extraDetails +
            "'" +
            "}"
        );
    }

    public StockLevel update() {
        return update(this);
    }

    public StockLevel persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static StockLevel update(StockLevel stockLevel) {
        if (stockLevel == null) {
            throw new IllegalArgumentException("stockLevel can't be null");
        }
        var entity = StockLevel.<StockLevel>findById(stockLevel.id);
        if (entity != null) {
            entity.lastModifiedDate = stockLevel.lastModifiedDate;
            entity.ramainingQuantity = stockLevel.ramainingQuantity;
            entity.extraDetails = stockLevel.extraDetails;
            entity.warehouse = stockLevel.warehouse;
            entity.product = stockLevel.product;
        }
        return entity;
    }

    public static StockLevel persistOrUpdate(StockLevel stockLevel) {
        if (stockLevel == null) {
            throw new IllegalArgumentException("stockLevel can't be null");
        }
        if (stockLevel.id == null) {
            persist(stockLevel);
            return stockLevel;
        } else {
            return update(stockLevel);
        }
    }
}
