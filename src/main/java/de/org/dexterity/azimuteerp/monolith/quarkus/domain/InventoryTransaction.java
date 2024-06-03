package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A InventoryTransaction.
 */
@Entity
@Table(name = "tb_inventory_transaction")
@Cacheable
@RegisterForReflection
public class InventoryTransaction extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "invoice_id", nullable = false)
    public Long invoiceId;

    @Column(name = "transaction_created_date")
    public Instant transactionCreatedDate;

    @Column(name = "transaction_modified_date")
    public Instant transactionModifiedDate;

    @NotNull
    @Column(name = "quantity", nullable = false)
    public Integer quantity;

    @Size(max = 255)
    @Column(name = "comments", length = 255)
    public String comments;

    @ManyToOne(optional = false)
    @JoinColumn(name = "supplier_id")
    @NotNull
    public Supplier supplier;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    @NotNull
    public Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_id")
    @NotNull
    public Warehouse warehouse;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryTransaction)) {
            return false;
        }
        return id != null && id.equals(((InventoryTransaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "InventoryTransaction{" +
            "id=" +
            id +
            ", invoiceId=" +
            invoiceId +
            ", transactionCreatedDate='" +
            transactionCreatedDate +
            "'" +
            ", transactionModifiedDate='" +
            transactionModifiedDate +
            "'" +
            ", quantity=" +
            quantity +
            ", comments='" +
            comments +
            "'" +
            "}"
        );
    }

    public InventoryTransaction update() {
        return update(this);
    }

    public InventoryTransaction persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static InventoryTransaction update(InventoryTransaction inventoryTransaction) {
        if (inventoryTransaction == null) {
            throw new IllegalArgumentException("inventoryTransaction can't be null");
        }
        var entity = InventoryTransaction.<InventoryTransaction>findById(inventoryTransaction.id);
        if (entity != null) {
            entity.invoiceId = inventoryTransaction.invoiceId;
            entity.transactionCreatedDate = inventoryTransaction.transactionCreatedDate;
            entity.transactionModifiedDate = inventoryTransaction.transactionModifiedDate;
            entity.quantity = inventoryTransaction.quantity;
            entity.comments = inventoryTransaction.comments;
            entity.supplier = inventoryTransaction.supplier;
            entity.product = inventoryTransaction.product;
            entity.warehouse = inventoryTransaction.warehouse;
        }
        return entity;
    }

    public static InventoryTransaction persistOrUpdate(InventoryTransaction inventoryTransaction) {
        if (inventoryTransaction == null) {
            throw new IllegalArgumentException("inventoryTransaction can't be null");
        }
        if (inventoryTransaction.id == null) {
            persist(inventoryTransaction);
            return inventoryTransaction;
        } else {
            return update(inventoryTransaction);
        }
    }
}
