package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.SizeOptionEnum;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Article.
 */
@Entity
@Table(name = "tb_article")
@Cacheable
@RegisterForReflection
public class Article extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "inventory_product_id", nullable = false)
    public Long inventoryProductId;

    @Size(max = 150)
    @Column(name = "custom_name", length = 150)
    public String customName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "custom_description")
    public String customDescription;

    @Column(name = "price_value", precision = 21, scale = 2)
    public BigDecimal priceValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_size", nullable = false)
    public SizeOptionEnum itemSize;

    @Size(max = 255)
    @Column(name = "assets_collection_uuid", length = 255)
    public String assetsCollectionUUID;

    @Column(name = "is_enabled")
    public Boolean isEnabled;

    @ManyToOne
    @JoinColumn(name = "main_category_id")
    public Category mainCategory;

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<OrderItem> ordersItemsLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Article{" +
            "id=" +
            id +
            ", inventoryProductId=" +
            inventoryProductId +
            ", customName='" +
            customName +
            "'" +
            ", customDescription='" +
            customDescription +
            "'" +
            ", priceValue=" +
            priceValue +
            ", itemSize='" +
            itemSize +
            "'" +
            ", assetsCollectionUUID='" +
            assetsCollectionUUID +
            "'" +
            ", isEnabled='" +
            isEnabled +
            "'" +
            "}"
        );
    }

    public Article update() {
        return update(this);
    }

    public Article persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Article update(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("article can't be null");
        }
        var entity = Article.<Article>findById(article.id);
        if (entity != null) {
            entity.inventoryProductId = article.inventoryProductId;
            entity.customName = article.customName;
            entity.customDescription = article.customDescription;
            entity.priceValue = article.priceValue;
            entity.itemSize = article.itemSize;
            entity.assetsCollectionUUID = article.assetsCollectionUUID;
            entity.isEnabled = article.isEnabled;
            entity.mainCategory = article.mainCategory;
            entity.ordersItemsLists = article.ordersItemsLists;
        }
        return entity;
    }

    public static Article persistOrUpdate(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("article can't be null");
        }
        if (article.id == null) {
            persist(article);
            return article;
        } else {
            return update(article);
        }
    }
}
