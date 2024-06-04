package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

/**
 * A Product.
 */
@Entity
@Table(name = "tb_product")
@Cacheable
@RegisterForReflection
public class Product extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Size(max = 25)
    @Column(name = "product_sku", length = 25)
    public String productSKU;

    @Size(max = 50)
    @Column(name = "product_name", length = 50)
    public String productName;

    @Lob
    // @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String description;

    @Column(name = "standard_cost", precision = 21, scale = 2)
    public BigDecimal standardCost;

    @NotNull
    @Column(name = "list_price", precision = 21, scale = 2, nullable = false)
    public BigDecimal listPrice;

    @Column(name = "reorder_level")
    public Integer reorderLevel;

    @Column(name = "target_level")
    public Integer targetLevel;

    @Size(max = 50)
    @Column(name = "quantity_per_unit", length = 50)
    public String quantityPerUnit;

    @NotNull
    @Column(name = "discontinued", nullable = false)
    public Boolean discontinued;

    @Column(name = "minimum_reorder_quantity")
    public Integer minimumReorderQuantity;

    @Size(max = 50)
    @Column(name = "suggested_category", length = 50)
    public String suggestedCategory;

    @Lob
    @Column(name = "attachments")
    public byte[] attachments;

    @Column(name = "attachments_content_type")
    public String attachmentsContentType;

    @Lob
    //@Type(type = "org.hibernate.type.TextType")
    @Column(name = "supplier_ids", columnDefinition = "TEXT")
    @JdbcType(LongVarcharJdbcType.class)
    public String supplierIds;

    @ManyToOne(optional = false)
    @JoinColumn(name = "brand_id")
    @NotNull
    public Brand brand;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<InventoryTransaction> productsLists = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<StockLevel> stockLevelsLists = new HashSet<>();

    @ManyToMany(mappedBy = "productsLists")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<Supplier> suppliersLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Product{" +
            "id=" +
            id +
            ", productSKU='" +
            productSKU +
            "'" +
            ", productName='" +
            productName +
            "'" +
            ", description='" +
            description +
            "'" +
            ", standardCost=" +
            standardCost +
            ", listPrice=" +
            listPrice +
            ", reorderLevel=" +
            reorderLevel +
            ", targetLevel=" +
            targetLevel +
            ", quantityPerUnit='" +
            quantityPerUnit +
            "'" +
            ", discontinued='" +
            discontinued +
            "'" +
            ", minimumReorderQuantity=" +
            minimumReorderQuantity +
            ", suggestedCategory='" +
            suggestedCategory +
            "'" +
            ", attachments='" +
            attachments +
            "'" +
            ", attachmentsContentType='" +
            attachmentsContentType +
            "'" +
            ", supplierIds='" +
            supplierIds +
            "'" +
            "}"
        );
    }

    public Product update() {
        return update(this);
    }

    public Product persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Product update(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product can't be null");
        }
        var entity = Product.<Product>findById(product.id);
        if (entity != null) {
            entity.productSKU = product.productSKU;
            entity.productName = product.productName;
            entity.description = product.description;
            entity.standardCost = product.standardCost;
            entity.listPrice = product.listPrice;
            entity.reorderLevel = product.reorderLevel;
            entity.targetLevel = product.targetLevel;
            entity.quantityPerUnit = product.quantityPerUnit;
            entity.discontinued = product.discontinued;
            entity.minimumReorderQuantity = product.minimumReorderQuantity;
            entity.suggestedCategory = product.suggestedCategory;
            entity.attachments = product.attachments;
            entity.supplierIds = product.supplierIds;
            entity.brand = product.brand;
            entity.productsLists = product.productsLists;
            entity.stockLevelsLists = product.stockLevelsLists;
            entity.suppliersLists = product.suppliersLists;
        }
        return entity;
    }

    public static Product persistOrUpdate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product can't be null");
        }
        if (product.id == null) {
            persist(product);
            return product;
        } else {
            return update(product);
        }
    }

    public static PanacheQuery<Product> findAllWithEagerRelationships() {
        return find("select distinct product from Product product");
    }

    public static Optional<Product> findOneWithEagerRelationships(Long id) {
        return find("select product from Product product where product.id =?1", id).firstResultOptional();
    }
}
